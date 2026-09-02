package com.premiumweather.app

import com.premiumweather.app.domain.mapper.AppError
import com.premiumweather.app.domain.mapper.ErrorMapper
import org.junit.Assert.*
import org.junit.Test
import java.net.UnknownHostException

class ErrorMapperTest {
    @Test fun networkError() {
        val err = ErrorMapper.map(UnknownHostException())
        assertTrue(err is AppError.Network)
    }
    @Test fun rateLimited() {
        val err = ErrorMapper.map(java.io.IOException("HTTP 429 Too Many Requests"))
        assertTrue(err is AppError.RateLimited)
    }
}
