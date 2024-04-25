package com.kounalem.moviedatabase.core.ui

import androidx.compose.runtime.Composable
import com.airbnb.android.showkase.models.ShowkaseBrowserComponent
import com.kounalem.moviedatabase.core.ui.ScreenshotPreview

/**
 * Preview wrapping [ShowkaseBrowserComponent], accessing its content and overriding toString() to
 * set proper naming for screenshot file.
 *
 * @property showkaseBrowserComponent component which was acquired and passed by showkase
 */

class ComponentPreview(
    private val showkaseBrowserComponent: ShowkaseBrowserComponent,
) : ScreenshotPreview {
    override val content: @Composable () -> Unit = showkaseBrowserComponent.component
    override fun toString(): String = "component=${
        listOfNotNull(
            showkaseBrowserComponent.group,
            showkaseBrowserComponent.componentName,
            showkaseBrowserComponent.styleName,
        ).joinToString(":")
    }"
}

