package screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kounalem.moviedatabase.core.ui.MovieDatabaseTheme
import com.kounalem.moviedatabase.datastore.UserPreferencesRepository
import com.kounalem.moviedatabase.managers.FeatureFlags
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DevActivity : ComponentActivity() {
    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    internal lateinit var featureFlags: FeatureFlags

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDatabaseTheme {
                val navController: NavHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DebugScreen(
                        navController = navController,
                        activity = this,
                        userPreferencesRepository = userPreferencesRepository,
                        featureFlags = featureFlags,
                    )
                }
            }
        }
    }
}
