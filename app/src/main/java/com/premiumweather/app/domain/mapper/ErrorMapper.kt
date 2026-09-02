package com.premiumweather.app.domain.mapper

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppError {
    object Network : AppError()
    object Timeout : AppError()
    object RateLimited : AppError()
    object Server : AppError()
    object NotFound : AppError()
    object Malformed : AppError()
    object LocationUnavailable : AppError()
    data class Unknown(val message: String) : AppError()
}

object ErrorMapper {
    fun map(throwable: Throwable): AppError {
        return when(throwable) {
            is UnknownHostException -> AppError.Network
            is SocketTimeoutException -> AppError.Timeout
            is IOException -> AppError.Network
            else -> {
                val msg = throwable.message ?: ""
                when {
                    msg.contains("429") -> AppError.RateLimited
                    msg.contains("400") -> AppError.NotFound
                    msg.contains("5") -> AppError.Server
                    else -> AppError.Unknown(msg)
                }
            }
        }
    }
    fun message(error: AppError): String {
        return when(error) {
            AppError.Network -> "No internet connection"
            AppError.Timeout -> "Request timed out"
            AppError.RateLimited -> "Too many requests, try later"
            AppError.Server -> "Server error"
            AppError.NotFound -> "Data not found"
            AppError.Malformed -> "Invalid data format"
            AppError.LocationUnavailable -> "Location unavailable"
            is AppError.Unknown -> "Unexpected error"
        }
    }
}
