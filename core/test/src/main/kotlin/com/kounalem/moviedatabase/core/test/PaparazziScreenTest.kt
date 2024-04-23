package com.kounalem.moviedatabase.core.test

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import androidx.compose.foundation.layout.Box
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.airbnb.android.showkase.models.ShowkaseElementsMetadata
import org.junit.Test

/**
 * Class with common [Paparazzi] settings for screenshot tests.
 *
 * @param config configuration for given test (device, night mode, font scale)
 */
@RunWith(Parameterized::class)
//abstract class PaparazziScreenTest(config: TestConfig) {
abstract  class PaparazziScreenTest(private val testPreview: TestPreview,){
    /**
     * Paparazzi configuration.
     */
//    @get:Rule
//    val paparazzi = Paparazzi(
//        maxPercentDifference = 0.0,
//        showSystemUi = false,
//        deviceConfig = when (config.device) {
//            Device.PIXEL_6 -> DeviceConfig.PIXEL_6
//            Device.PIXEL_C -> DeviceConfig.PIXEL_C
//        }.copy(
//            nightMode = config.nightMode,
//            fontScale = config.fontScale,
//        ),
//        renderingMode = SessionParams.RenderingMode.NORMAL,
//    )
//
//    /**
//     * Function to provide custom Parameterized runner to run tests over collection of given
//     * attributes. There is one parameter [TestConfig] that specifies configuration for each test.
//     *
//     * @return collection of arrays which contain theme and font scale
//     */
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(name = "{0}")
//        fun data(): Collection<Array<Any>> {
//            val devices = listOf(Device.PIXEL_6, Device.PIXEL_C)
//            val fontScales = listOf(1f, 1.5f)
//            val modes = listOf(NightMode.NIGHT, NightMode.NOTNIGHT)
//
//            return devices.flatMap { device ->
//                fontScales.flatMap { fontScale ->
//                    modes.mapNotNull { mode ->
//                        // Filter combination of dark mode and font scale 150% because it is unnecessary
//                        if (mode == NightMode.NIGHT && fontScale == 1.5f) {
//                            null
//                        } else {
//                            arrayOf(TestConfig(device, mode, fontScale))
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * Function to run paparazzi screenshot with given composable content using composition provider.
//     *
//     * @param content composable content of screen preview
//     */
////    fun screenshotTest(content: @Composable () -> Unit) {
////        paparazzi.snapshot {
////            content.invoke()
////        }
////    }
//    fun screenshotTest(content: @Composable () -> Unit) {
//        paparazzi.snapshot(content.toString().substringAfter('_')) {
//            val lifecycleOwner = LocalLifecycleOwner.current
//            CompositionLocalProvider(
//                LocalInspectionMode provides true,
//                // Needed so that UI that uses it don't crash during screenshot tests
//                LocalOnBackPressedDispatcherOwner provides object : OnBackPressedDispatcherOwner {
//                    override val lifecycle = lifecycleOwner.lifecycle
//
//                    override val onBackPressedDispatcher = OnBackPressedDispatcher()
//                },
//            ) {
//                Box {
//                    content.invoke()
//                }
//            }
//        }
//    }


    companion object {
        fun provideValues(
            metadata: ShowkaseElementsMetadata,
            packageName: String,
        ): Collection<Array<Any>> {
            return metadata.componentList.filter {
                it.componentKey.startsWith(packageName)
            }.toSet().map {
                arrayOf(
                    ComponentTestPreview(it),
                    when {
                        // If there is a style name then add that to the test name
                        !it.styleName.isNullOrBlank() -> "${it.componentName} - ${it.styleName}"
                        // If the key ends in an index then this has a preview parameter so need to
                        // add that on
                        it.componentKey.matches(".*_(\\d+)$".toRegex()) -> {
                            val idx = ".*_(\\d+)$".toRegex().find(it.componentKey)!!.groupValues[1]
                            "${it.componentName} - $idx"
                        }
                        // Otherwise just use the name
                        else -> it.componentName
                    },
                )
            }
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        deviceConfig = DeviceConfig.PIXEL_5.copy(softButtons = false, locale = "en-rGB"),
    )

    @Test
    fun screenshotTest() {
        paparazzi.snapshot(
            // The toString representation starts with the package name of the preview but this just
            // adds extra length and makes it more likely to go over the filename limit.
            // Remove the package now
            testPreview.toString().substringAfter('_'),
        ) {
            val lifecycleOwner = LocalLifecycleOwner.current
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                // Needed so that UI that uses it don't crash during screenshot tests
                LocalOnBackPressedDispatcherOwner provides object : OnBackPressedDispatcherOwner {
                    override val lifecycle = lifecycleOwner.lifecycle

                    override val onBackPressedDispatcher = OnBackPressedDispatcher()
                },
            ) {
                Box {
                    testPreview.Content()
                }
            }
        }
    }
}

interface TestPreview {
    @Composable
    fun Content()
}

class ComponentTestPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent,
) : TestPreview {
    @Composable
    override fun Content() = showkaseBrowserComponent.component()
    override fun toString(): String = showkaseBrowserComponent.componentKey
}

