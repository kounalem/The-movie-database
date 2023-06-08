package di

import DevMenuSensorDelegateIml
import android.content.Context
import com.kounalem.moviedatabase.debug.DevMenuSensorDelegate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugModule {
    @Provides
    @Singleton
    fun provideDeveloperDelegate(
        @ApplicationContext context: Context,
    ): DevMenuSensorDelegate = DevMenuSensorDelegateIml(context)

}
