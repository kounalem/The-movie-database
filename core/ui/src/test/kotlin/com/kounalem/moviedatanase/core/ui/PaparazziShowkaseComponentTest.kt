package com.kounalem.moviedatanase.core.ui

import com.airbnb.android.showkase.models.Showkase
import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestConfig
import org.junit.runners.Parameterized

/**
 * Test class for screenshot testing of components that are part of this module and contain showkase
 * annotation (@ShowkaseComposable).
 */
class ComponentTests(
    componentPreview: TestPreview,
    config: TestConfig,
) : PaparazziShowkaseTest<TestPreview>(componentPreview, config) {
    companion object {
        /**
         * Function to provide custom Parameterized runner to run tests over collection of given
         * attributes. There are two parameters of [ComponentPreview] and [TestConfig].
         *
         * @return combinations of these parameters as collection of arrays
         */
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun provideValues(): Collection<Array<Any>> =
            PaparazziScreenTest.provideValues(
                Showkase.getMetadata(),
                "com.kounalem.moviedatabase.core.ui",
            )
    }
}
