package com.kounalem.moviedatabase.core.ui

import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import com.kounalem.moviedatabase.core.ui.components.ListItemPreview
import com.kounalem.moviedatabase.core.ui.components.MovieOutlinedTextFieldPreview
import com.kounalem.moviedatabase.core.ui.components.MovieTopAppBarPreview
import com.kounalem.moviedatabase.core.ui.components.PTextPreview
import com.kounalem.moviedatabase.core.ui.components.PillPreview
import com.kounalem.moviedatabase.core.ui.components.ToggleListItemPreview
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Paparazzi screenshot tests for the @ScreenPreview-annotated components in core:ui.
 * Each test calls the corresponding public *Preview() function, which wraps content in
 * [com.kounalem.moviedatabase.core.ui.theming.PreviewBox] so the theme is applied.
 */
@RunWith(Parameterized::class)
class ComponentTests(config: TestConfig) : PaparazziScreenTest(config) {

    @Test fun topAppBar() = screenshotTest { MovieTopAppBarPreview() }

    @Test fun outlinedTextField() = screenshotTest { MovieOutlinedTextFieldPreview() }

    @Test fun pill() = screenshotTest { PillPreview() }

    @Test fun text() = screenshotTest { PTextPreview() }

    @Test fun listItem() = screenshotTest { ListItemPreview() }

    @Test fun listToggleItem() = screenshotTest { ToggleListItemPreview() }
}
