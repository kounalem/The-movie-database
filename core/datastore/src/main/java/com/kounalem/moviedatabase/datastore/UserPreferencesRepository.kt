package com.kounalem.moviedatabase.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import com.kounalem.moviedatabase.domain.models.EnvironmentConfig
import com.kounalem.moviedatabase.domain.models.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {

    val userData = userPreferences.data
        .map {
            UserData(
                environment = when (it.environment) {
                    Environment.Prod -> EnvironmentConfig.Prod
                    Environment.Dev -> EnvironmentConfig.Dev
                    Environment.Staging -> EnvironmentConfig.Staging
                    Environment.Local -> EnvironmentConfig.Local
                    Environment.UNRECOGNIZED -> EnvironmentConfig.Prod
                    null -> EnvironmentConfig.Prod
                }
            )
        }

    suspend fun setEnvironment(environment: EnvironmentConfig) {
        try {
            userPreferences.updateData {
                it.copy {
                    this.environment = when (environment) {
                        EnvironmentConfig.Prod -> Environment.Prod
                        EnvironmentConfig.Dev -> Environment.Dev
                        EnvironmentConfig.Staging -> Environment.Staging
                        EnvironmentConfig.Local -> Environment.Local
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("Movie DB preferences", "Failed to update user preferences", ioException)
        }
    }
}
