package di

import android.app.Activity
import com.kounalem.moviedatabase.debug.DevMenuSensorDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReleaseAppModule {
//    @Provides
//    @Singleton
//    fun provideDevReleaseDelegate(
//    ): DevMenuSensorDelegate =
//        object : DevMenuSensorDelegate {
//            override fun registerShakeListener(activity: Activity) {
//                // do nothing
//            }
//
//            override fun removeShakeListener() {
//                // do nothing
//            }
//        }

}
