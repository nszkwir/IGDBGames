package com.spitzer.igdbgames.core.network

import retrofit2.HttpException
import retrofit2.Response

internal suspend fun <T> safeCall(call: suspend () -> Response<T>): ResultData<T?> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let { ResultData.Success(response.body(), response.code()) }
                ?: run { ResultData.Error(Exception(response.message()), response.code()) }
        } else {
            ResultData.Error(HttpException(response), response.code())
        }
    } catch (e: Exception) {
        ResultData.Error(e)
    }
}
