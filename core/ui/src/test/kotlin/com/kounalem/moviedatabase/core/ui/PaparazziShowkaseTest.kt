package com.kounalem.moviedatabase.core.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import com.kounalem.moviedatabase.core.test.Device
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import com.android.ide.common.rendering.api.SessionParams
import com.kounalem.moviedatabase.core.ui.ScreenshotPreview

@RunWith(Parameterized::class)
abstract class PaparazziShowkaseTest<T : ScreenshotPreview>(
    private val preview: T,
    config: TestConfig = TestConfig(Device.PIXEL_6, NightMode.NOTNIGHT, 1f),
) {
    /**
     * Paparazzi configuration.
     */
    @get:Rule
    val paparazzi = Paparazzi(
        maxPercentDifference = 0.0,
        showSystemUi = false,
        deviceConfig = when (config.device) {
            Device.PIXEL_6 -> DeviceConfig.PIXEL_6
            Device.PIXEL_C -> DeviceConfig.PIXEL_C
        }.copy(
            nightMode = config.nightMode,
            fontScale = config.fontScale,
        ),
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    /**
     * Test to run paparazzi screenshot with given composition configuration.
     */
    @Test
    fun screenshotTest() {
        paparazzi.snapshot {
            preview.content.invoke()
        }
    }
}
