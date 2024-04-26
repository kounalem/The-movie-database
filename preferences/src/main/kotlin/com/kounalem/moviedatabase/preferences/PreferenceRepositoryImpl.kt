package com.kounalem.moviedatabase.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.core.content.edit
import com.kounalem.moviedatabase.preferences.di.SecureSharedPrefsFile
import com.kounalem.moviedatabase.preferences.di.SharedPrefsFile
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
internal class PreferenceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @SharedPrefsFile sharedPrefsFileName: String,
    @SecureSharedPrefsFile securePrefsFileName: String,
) : PreferenceRepository {

    private val flowItems =
        mutableMapOf<String, List<Triple<KClass<out Any>, Any, ProducerScope<Any>>>>()

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { sp, key ->
        flowItems[key]?.forEach { (klass, default, producer) ->
            emitFromPrefs(sp, key, klass, default, producer)
        }
    }

    private val preferences by lazy {
        context.getSharedPreferences(
            sharedPrefsFileName,
            Context.MODE_PRIVATE,
        )
    }
    private val secPrefs by lazy {
        SecurePrefs(context, securePrefsFileName, k, true)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return preferences.getBoolean(key, default)
    }

    fun hasKey(prefName: String): Boolean {
        return preferences.contains(prefName)
    }

    override fun getString(key: String, default: String): String {
        return when (key) {
            PREF_USER_ENCRYPTED_TOKEN,
            -> secPrefs.getString(key)

            else -> preferences.getString(key, default) ?: default
        }
    }

    override fun getStringSet(key: String, default: Set<String>): Set<String> {
        return preferences.getStringSet(key, default) ?: default
    }

    override fun getInt(key: String, default: Int): Int {
        return preferences.getInt(key, default)
    }

    @Suppress("unused")
    fun getFloat(prefName: String, defaultValue: Float): Float {
        return preferences.getFloat(prefName, defaultValue)
    }

    override fun getLong(key: String, default: Long): Long {
        return preferences.getLong(key, default)
    }

    @Suppress("UNCHECKED_CAST")
    private fun emitFromPrefs(
        sp: SharedPreferences,
        key: String?,
        klass: KClass<out Any>,
        default: Any,
        producer: ProducerScope<Any>,
    ) {
        val result = when (klass) {
            Int::class -> producer.trySend(sp.getInt(key, default as Int))
            String::class -> producer.trySend(
                sp.getString(key, default as String) ?: default,
            )

            Long::class -> producer.trySend(sp.getLong(key, default as Long))
            Boolean::class -> producer.trySend(sp.getBoolean(key, default as Boolean))
            else -> {
                if (default is Set<*>) {
                    val safeIshDefault: Set<String> = default as? Set<String> ?: emptySet()
                    sp.getStringSet(key, safeIshDefault)?.let { producer.trySend(it) }
                } else {
                    TODO()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : Any> flow(
        prefName: String,
        defaultValue: T,
        emitImmediately: Boolean,
    ): Flow<T> {
        if (prefName in listOf(
                PREF_USER_ENCRYPTED_TOKEN,
            )
        ) {
            return secPrefs.stringFlow(prefName, defaultValue as String, emitImmediately) as Flow<T>
        }

        return callbackFlow {
            val triple = Triple(T::class, defaultValue, this)
            synchronized(flowItems) {
                if (flowItems.isEmpty()) {
                    preferences.registerOnSharedPreferenceChangeListener(prefsListener)
                }
                flowItems[prefName] = flowItems.getOrPut(prefName) { emptyList() }
                    .plus(triple)
            }

            if (emitImmediately) {
                emitFromPrefs(preferences, prefName, T::class, defaultValue, this)
            }

            awaitClose {
                synchronized(flowItems) {
                    flowItems[prefName]?.let { items ->
                        val newItems = items.minus(triple)
                        if (newItems.isEmpty()) {
                            flowItems.remove(prefName)
                            if (flowItems.isEmpty()) {
                                preferences.unregisterOnSharedPreferenceChangeListener(
                                    prefsListener,
                                )
                            }
                        } else {
                            flowItems[prefName] = newItems
                        }
                    }
                }
            }
        } as Flow<T>
    }

    override fun flowString(
        key: String,
        default: String,
        emitImmediately: Boolean,
    ): Flow<String> {
        return flow(key, default, emitImmediately)
    }

    override fun flowInt(
        key: String,
        default: Int,
        emitImmediately: Boolean,
    ): Flow<Int> {
        return flow(key, default, emitImmediately)
    }

    override fun flowLong(
        key: String,
        default: Long,
        emitImmediately: Boolean,
    ): Flow<Long> {
        return flow(key, default, emitImmediately)
    }

    override fun flowBoolean(
        key: String,
        default: Boolean,
        emitImmediately: Boolean,
    ): Flow<Boolean> {
        return flow(key, default, emitImmediately)
    }

    override fun flowStringSet(
        key: String,
        default: Set<String>,
        emitImmediately: Boolean,
    ): Flow<Set<String>> {
        return flow(key, default, emitImmediately)
    }

    /**
     * Remove all the keys in shared preferences. Used to clear shared prefs during tests
     */
    @VisibleForTesting
    fun removeAll() {
        preferences.edit { clear() }
    }

    override fun remove(key: String) {
        removeEntry(key)
    }

    override fun removeEntry(prefName: String) {
        when (prefName) {
            PREF_USER_ENCRYPTED_TOKEN -> secPrefs.removeValue(
                prefName,
            )

            else -> preferences.edit().remove(prefName).apply()
        }
    }

    fun batchInsert(keyValues: List<Pair<String, Any?>>) {
        val e = preferences.edit()
        for (value in keyValues) {
            insertTypedValue(value.first, value.second, e)
        }
        e.apply()
    }

    override fun insert(key: String, value: Any?) {
        val e = preferences.edit()
        insertTypedValue(key, value, e)
        e.apply()
    }

    private fun insertTypedValue(key: String, value: Any?, e: SharedPreferences.Editor) {
        when (key) {
            PREF_USER_ENCRYPTED_TOKEN -> secPrefs.put(
                key,
                value as String,
            )

            else -> {
                @Suppress("UNCHECKED_CAST")
                when (value) {
                    is String -> e.putString(key, value)
                    is Boolean -> e.putBoolean(key, value)
                    is Int -> e.putInt(key, value)
                    is Float -> e.putFloat(key, value)
                    is Long -> e.putLong(key, value)
                    is Set<*> -> e.putStringSet(key, value as Set<String>?)
                }
            }
        }
    }

    fun clearAll() {
        preferences.edit().clear().apply()
        secPrefs.clear()
    }

    companion object {
        private const val k = "kkmmkk"
        private const val PREF_USER_ENCRYPTED_TOKEN = "MM_ENCRYPTED"

        @JvmStatic
        fun getInstance(context: Context): PreferenceRepositoryImpl {
            return EntryPoints.get(
                context.applicationContext,
                InjectDelegate::class.java,
            ).getSharedPreferences()
        }

        @EntryPoint
        @InstallIn(SingletonComponent::class)
        interface InjectDelegate {
            fun getSharedPreferences(): PreferenceRepositoryImpl
        }
    }
}
