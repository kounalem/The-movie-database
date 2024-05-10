package screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kounalem.moviedatabase.core.ui.components.MovieListSingleChoiceToggleItem
import com.kounalem.moviedatabase.core.ui.components.MovieListToggleItem
import com.kounalem.moviedatabase.core.ui.components.MovieText
import com.kounalem.moviedatabase.core.ui.theming.large
import com.kounalem.moviedatabase.core.ui.showShowkase
import com.kounalem.moviedatabase.core.ui.theming.xlarge
import com.kounalem.moviedatabase.datastore.UserPreferencesRepository
import com.kounalem.moviedatabase.domain.models.EnvironmentConfig
import com.kounalem.moviedatabase.domain.models.UserData
import com.kounalem.moviedatabase.managers.FeatureFlags

@Composable
fun DebugScreen(
    navController: NavHostController,
    activity: Activity,
    userPreferencesRepository: UserPreferencesRepository,
    featureFlags: FeatureFlags
) {

    val userData by userPreferencesRepository.userData
        .collectAsStateWithLifecycle(initialValue = UserData(EnvironmentConfig.Dev))

    var currentEnv by remember { mutableStateOf(userData.environment) }
    LaunchedEffect(userData) {
        currentEnv = userData.environment
    }
    LaunchedEffect(currentEnv) {
        if (currentEnv != userData.environment)
            userPreferencesRepository.setEnvironment(currentEnv)
    }

    Column(modifier = Modifier.padding(top = xlarge)) {
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large), onClick = {
            showShowkase(context = activity.baseContext, onShowkaseMissing = {
                Log.d(
                    "Moviedatabase", "Showkase is only available on debug builds"
                )
            })
        }) {
            MovieText(text = "Go to showcase", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.padding(vertical = xlarge))
        SelectEnvironment(
            currentEnv = currentEnv,
            updateConfig = { newConfig ->
                currentEnv = newConfig
            }
        )

        Spacer(modifier = Modifier.padding(vertical = xlarge))
        FeatureFlags(featureFlags)
    }

}

@Composable
private fun SelectEnvironment(
    currentEnv: EnvironmentConfig,
    updateConfig: (config: EnvironmentConfig) -> Unit,
) {
    MovieText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
        text = "Select environment:",
        style = MaterialTheme.typography.titleMedium
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
    ) {
        items(
            listOf(
                EnvironmentConfig.Prod,
                EnvironmentConfig.Dev,
                EnvironmentConfig.Staging,
                EnvironmentConfig.Local
            ),
            key = { it.name }
        )
        { item ->
            MovieListSingleChoiceToggleItem(
                title = item.name,
                description = null,
                toggleInitValue = currentEnv == item,
                onToggle = {
                    if (it)
                        updateConfig(item)
                }
            )
        }
    }
}

@Composable
private fun FeatureFlags(featureFlags: FeatureFlags) {
    val flags = FeatureFlags::class.java.declaredFields.mapNotNull { field ->
        if (FeatureFlags.FeatureFlag::class.java.isAssignableFrom(field.type)) {
            field.isAccessible = true
            field.get(featureFlags) as FeatureFlags.FeatureFlag
        } else {
            null
        }
    }

    MovieText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
        text = "Feature Flags:",
        style = MaterialTheme.typography.titleMedium
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
    ) {
        items(
            flags,
            key = { it.name }
        )
        { item ->
            MovieListToggleItem(
                title = item.name,
                description = null,
                toggleInitValue = item.isFlagSet(),
                onToggle = {
                    item.setLocalFlag(it)
                }
            )
        }
    }
}
