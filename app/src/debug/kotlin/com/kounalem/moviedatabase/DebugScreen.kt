package com.kounalem.moviedatabase

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kounalem.moviedatabase.core.ui.large
import com.kounalem.moviedatabase.core.ui.showShowkase
import com.kounalem.moviedatabase.core.ui.xlarge

@Composable
fun DebugScreen(navController: NavHostController, activity: Activity) {

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
    }

}
