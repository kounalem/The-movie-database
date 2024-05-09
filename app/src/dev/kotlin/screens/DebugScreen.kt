package screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kounalem.moviedatabase.core.ui.components.ListToggleItem
import com.kounalem.moviedatabase.core.ui.large
import com.kounalem.moviedatabase.core.ui.showShowkase
import com.kounalem.moviedatabase.core.ui.xlarge
import com.kounalem.moviedatabase.datastore.UserPreferencesRepository
import com.kounalem.moviedatabase.domain.models.EnvironmentConfig
import com.kounalem.moviedatabase.domain.models.UserData
import com.kounalem.moviedatabase.managers.FeatureFlags
import kotlinx.coroutines.CoroutineScope

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
            Text(text = "Go to showcase")
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
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large), text = "Select environment:"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
    ) {
        itemsIndexed(
            listOf(
                EnvironmentConfig.Prod,
                EnvironmentConfig.Dev,
                EnvironmentConfig.Staging,
                EnvironmentConfig.Local
            )
        ) { _, item ->
            ListToggleItem(
                title = item.name,
                description = null,
                toggleInitValue = currentEnv == item,
                toggled = {
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

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
        text = "Feature Flags:",
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
    ) {
        itemsIndexed(flags) { _, item ->
            ListToggleItem(
                title = item.name,
                description = null,
                toggleInitValue = item.isFlagSet(),
                toggled = {
                    item.setLocalFlag(it)
                }
            )
        }
    }
}
