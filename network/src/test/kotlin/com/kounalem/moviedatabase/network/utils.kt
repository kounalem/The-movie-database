package com.kounalem.moviedatabase.network

import io.mockk.every
import io.mockk.mockk
import retrofit2.Response

internal fun <T> T.dtoToResponse() =
    mockk<Response<T>> {
        every { isSuccessful } returns true
        every { body() } returns this@dtoToResponse
    }
