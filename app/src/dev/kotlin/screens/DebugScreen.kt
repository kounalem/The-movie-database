package screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kounalem.moviedatabase.core.ui.large
import com.kounalem.moviedatabase.core.ui.showShowkase
import com.kounalem.moviedatabase.core.ui.small
import com.kounalem.moviedatabase.core.ui.xlarge
import com.kounalem.moviedatabase.managers.FeatureFlags
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Local
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Prod
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Staging
import com.kounalem.moviedatabase.preferences.PreferenceRepository

@Composable
fun DebugScreen(
    navController: NavHostController,
    activity: Activity,
    preferenceRepository: PreferenceRepository,
    featureFlags: FeatureFlags
) {

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
        SelectEnvironment(preferenceRepository)

        Spacer(modifier = Modifier.padding(vertical = xlarge))
        FeatureFlags(featureFlags)
    }

}

@Composable
private fun SelectEnvironment(preferenceRepository: PreferenceRepository) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large), text = "Select environment:"
    )
    val environmentId = preferenceRepository.getString(PreferenceRepository.ENVIRONMENT)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = large),
    ) {
        itemsIndexed(listOf(Prod, Local, Staging, Prod)) { _, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                val checkedState = remember { mutableStateOf(environmentId == item.id) }
                Text(text = item.id)
                Spacer(modifier = Modifier.padding(horizontal = small))
                Switch(checked = checkedState.value, onCheckedChange = {
                    preferenceRepository.insert(PreferenceRepository.ENVIRONMENT, item.id)
                })
            }
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                val checkedState = remember { mutableStateOf(item.isFlagSet()) }
                Text(
                    text = item.name,
                )
                Spacer(modifier = Modifier.padding(horizontal = small))
                Switch(checked = checkedState.value, onCheckedChange = {
                    val updated = !checkedState.value
                    item.setLocalFlag(updated)
                    checkedState.value = updated
                })
            }
        }
    }
}
