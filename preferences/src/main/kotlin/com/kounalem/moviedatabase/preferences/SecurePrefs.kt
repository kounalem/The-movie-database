package com.kounalem.moviedatabase.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.util.Base64
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.UnsupportedEncodingException
import java.security.GeneralSecurityException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal class SecurePrefs(
    context: Context,
    preferenceName: String?,
    secureKey: String,
    private val encryptKeys: Boolean,
) {
    class SecurePreferencesException(e: Throwable?) : RuntimeException(e)

    private val writer: Cipher
    private val reader: Cipher
    private val keyWriter: Cipher
    private val preferences: SharedPreferences

    init {
        try {
            writer = Cipher.getInstance(TRANSFORMATION)
            reader = Cipher.getInstance(TRANSFORMATION)
            keyWriter = Cipher.getInstance(KEY_TRANSFORMATION)
            initCiphers(secureKey)
            preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        } catch (e: GeneralSecurityException) {
            throw SecurePreferencesException(e)
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
    )
    private fun initCiphers(secureKey: String) {
        val ivSpec = iv
        val secretKey = getSecretKey(secureKey)
        writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
        reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
        keyWriter.init(Cipher.ENCRYPT_MODE, secretKey)
    }

    private val iv: IvParameterSpec
        get() {
            val iv = ByteArray(writer.blockSize)
            System.arraycopy(
                "fldsjfodasjifudslfjdsaofshaufihadsf".toByteArray(),
                0,
                iv,
                0,
                writer.blockSize,
            )
            return IvParameterSpec(iv)
        }

    @Throws(UnsupportedEncodingException::class, NoSuchAlgorithmException::class)
    private fun getSecretKey(key: String): SecretKeySpec {
        val keyBytes = createKeyBytes(key)
        return SecretKeySpec(keyBytes, TRANSFORMATION)
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
    )
    private fun createKeyBytes(key: String): ByteArray {
        val md =
            MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION)
        md.reset()
        return md.digest(key.toByteArray(charset(CHARSET)))
    }

    fun put(key: String, value: String?) {
        if (value == null) {
            preferences.edit().remove(toKey(key)).apply()
        } else {
            putValue(toKey(key), value)
        }
    }

    fun containsKey(key: String): Boolean {
        return preferences.contains(toKey(key))
    }

    fun removeValue(key: String) {
        preferences.edit().remove(toKey(key)).apply()
    }

    @Throws(SecurePreferencesException::class)
    fun getString(key: String): String {
        val encodedKey = toKey(key)
        if (preferences.contains(encodedKey)) {
            val securedEncodedValue = preferences.getString(encodedKey, "")
            return decrypt(securedEncodedValue)
        }
        return ""
    }

    fun stringFlow(
        key: String,
        defaultValue: String,
        emitImmediately: Boolean,
    ): Flow<String> {
        val encodedKey = toKey(key)
        return callbackFlow {
            val listener = OnSharedPreferenceChangeListener { sharedPreferences, key1 ->
                if (key1 == encodedKey) {
                    val fromPrefs = sharedPreferences.getString(key1, null)
                    if (fromPrefs == null) {
                        trySend(defaultValue)
                    } else {
                        trySend(decrypt(fromPrefs))
                    }
                }
            }
            preferences.registerOnSharedPreferenceChangeListener(listener)

            if (emitImmediately) {
                val fromPrefs = preferences.getString(encodedKey, null)
                if (fromPrefs == null) {
                    trySend(defaultValue)
                } else {
                    trySend(decrypt(fromPrefs))
                }
            }

            awaitClose {
                preferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    private fun toKey(key: String): String {
        return if (encryptKeys) {
            synchronized(keyWriter) { return encrypt(key, keyWriter) }
        } else {
            key
        }
    }

    @Throws(SecurePreferencesException::class)
    private fun putValue(key: String, value: String) {
        var secureValueEncoded: String
        synchronized(writer) { secureValueEncoded = encrypt(value, writer) }
        preferences.edit().putString(key, secureValueEncoded).apply()
    }

    @Throws(SecurePreferencesException::class)
    private fun encrypt(value: String, writer: Cipher): String {
        val secureValue: ByteArray = try {
            convert(
                writer,
                value.toByteArray(charset(CHARSET)),
            )
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }
        return Base64.encodeToString(secureValue, Base64.NO_WRAP)
    }

    private fun decrypt(securedEncodedValue: String?): String {
        val securedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP)
        var value: ByteArray
        synchronized(reader) { value = convert(reader, securedValue) }
        return try {
            value.toString(charset(CHARSET))
        } catch (e: UnsupportedEncodingException) {
            throw SecurePreferencesException(e)
        }
    }

    @Throws(SecurePreferencesException::class)
    private fun convert(cipher: Cipher, bs: ByteArray): ByteArray {
        return try {
            cipher.doFinal(bs)
        } catch (e: Exception) {
            throw SecurePreferencesException(e)
        }
    }

    companion object {
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding"
        private const val SECRET_KEY_HASH_TRANSFORMATION = "SHA-256"
        private const val CHARSET = "UTF-8"
    }
}
