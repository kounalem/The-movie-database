package com.kounalem.moviedatabase.managers

import android.content.Context
import androidx.core.content.edit
import com.kounalem.moviedatabase.managers.FeatureFlags.Stage
import com.kounalem.moviedatabase.feature.movies.domain.usecase.PopularMoviesFeatureFlags
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty

/**
 * Class to provide feature flags to the rest of the app.
 *
 * A feature flag defines whether a feature is turned on or off, and is controlled locally and
 * remotely. For general users, this is only ever controlled remotely.
 *
 * Using a feature flag is done by reading one of the public vals as a boolean e.g.
 *
 * ```kotlin
 * if (featureFlags.isRichMediaEnabled) {}
 * ```
 *
 * A feature flag is specified by a name and a stage. The name is used to save the flag locally in
 * shared preferences, and to get the flag from firebase remote config. This means that the name
 * defined here *MUST* be the same as the field used on remote config.
 *
 * The stage of a feature flag controls it's default value and whether it listens to remote config.
 *
 * In [Stage.DEV], the flag does not listen to remote config and can only be set locally. The
 * default value if not set is `false`. This stage is used while a feature is in development.

 * In [Stage.RELEASE], the flag will listen to remote config, and if no remote config value is set
 * then it will default to `true`. Any locally set value will override this. This stage is used for
 * generally available features where there is a chance we may want to disable it in future. This
 * is a rare stage, and in most cases the feature flag can be deleted from the codebase (as if it
 * always returned true) rather than moving into this stage.
 *
 * Example feature flags:
 *
 * ```
 * val devTest by FeatureFlag("dev_test", Stage.DEV)
 * val releaseTest by FeatureFlag("release_test", Stage.RELEASE)
 * ```
 */
@Singleton
class FeatureFlags @Inject constructor(
    context: Context,
//   TODO create a config repo eg private val featureRemoteConfig: ConfigRepository,
    private val buildTypeInfo: com.kounalem.moviedatabase.config.BuildTypeInfo,
) : com.kounalem.moviedatabase.feature.movies.domain.usecase.PopularMoviesFeatureFlags {
    override val showFilterByFavourite by FeatureFlag(SHOW_FILTERED_MOVIES_BY_FAVOURITE, Stage.DEV)

    enum class Stage {
        DEV,
        RELEASE,
    }

    private val isDev by lazy { buildTypeInfo.isDev() }
    private val preferences by lazy {
        context.getSharedPreferences("feature_flags.prefs", Context.MODE_PRIVATE)
    }

    inner class FeatureFlag(
        val name: String,
        private val stage: Stage,
    ) {
        private val prefsName = "feature-flag.$name"

        operator fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return isFlagSet()
        }

        fun isFlagSet(): Boolean {
            val enabledInPrefs = if (isDev) fromPreferences() else null
            return when (stage) {
                Stage.DEV -> enabledInPrefs ?: false
                else ->
                    enabledInPrefs
                        ?: fromRemoteConfig()
                        ?: getDefault()
            }
        }

        fun setLocalFlag(value: Boolean?) {
            when (value) {
                null -> preferences.edit { remove(prefsName) }
                false -> preferences.edit { putInt(prefsName, 1) }
                true -> preferences.edit { putInt(prefsName, 2) }
            }
        }

        private fun fromRemoteConfig(): Boolean? {
//         TODO return the config's value   return if (featureRemoteConfig.hasValue(name)) {
//                featureRemoteConfig.getBoolean(name, false)
//            } else {
            return null
//            }
        }

        private fun getDefault(): Boolean = stage == Stage.RELEASE

        fun fromPreferences(): Boolean? {
            return when (preferences.getInt(prefsName, 0)) {
                0 -> null
                1 -> false
                2 -> true
                else -> throw IllegalStateException()
            }
        }
    }

    companion object {
        const val SHOW_FILTERED_MOVIES_BY_FAVOURITE = "showMoviesByFavourite"
    }
}
