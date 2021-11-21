package com.spitzer.igdbgames.core.room

internal suspend fun <T> localSafeCall(call: suspend () -> T): RoomResultData<T?> {
    return try {
        val data = call.invoke()
        data?.let { RoomResultData.Success(data) }
            ?: run { RoomResultData.Error(Exception("Room null data error")) }
    } catch (e: Exception) {
        RoomResultData.Error(e)
    }
}

internal suspend fun <T> localSafeInsert(call: suspend () -> T): RoomResultData<T?> {
    return try {
        val data = call.invoke()
        RoomResultData.Success(data)
    } catch (e: Exception) {
        RoomResultData.Error(e)
    }
}