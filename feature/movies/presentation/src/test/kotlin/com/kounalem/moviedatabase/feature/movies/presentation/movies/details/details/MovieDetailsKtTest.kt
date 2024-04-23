package com.kounalem.moviedatabase.feature.movies.presentation.movies.details.details

import com.airbnb.android.showkase.models.Showkase
import com.kounalem.moviedatabase.core.test.PaparazziScreenTest
import com.kounalem.moviedatabase.core.test.TestPreview
import com.kounalem.moviedatanase.core.ui.getMetadata
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
internal class MovieDetailsKtTest(
    componentTestPreview: TestPreview,
    @Suppress("UNUSED_PARAMETER")
    name: String, // Need this parameter to display test name nicely
) : PaparazziScreenTest(componentTestPreview) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun provideValues(): Collection<Array<Any>> =
            provideValues(
                Showkase.getMetadata(),
                "com.kounalem.moviedatabase.movies.details",
            )
    }

}
