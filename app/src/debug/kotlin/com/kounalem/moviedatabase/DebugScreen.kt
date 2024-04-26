package com.kounalem.moviedatabase

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kounalem.moviedatabase.core.ui.large
import com.kounalem.moviedatabase.core.ui.showShowkase
import com.kounalem.moviedatabase.core.ui.xlarge
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Local
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Prod
import com.kounalem.moviedatabase.network.ServerInfo.Companion.Staging
import com.kounalem.moviedatabase.preferences.PreferenceRepository

@Composable
fun DebugScreen(
    navController: NavHostController, activity: Activity, preferenceRepository: PreferenceRepository
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

        Spacer(modifier = Modifier.padding(start = large, top = large))
        Text(text = "Select environment:")

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = large),
        ) {
            itemsIndexed(listOf(Prod, Local, Staging, Prod)) { _, item ->
                Text(text = item.id,
                     modifier = Modifier.clickable {
                         preferenceRepository.insert(PreferenceRepository.ENVIRONMENT, item.id)
                     })
            }
        }
    }


}
