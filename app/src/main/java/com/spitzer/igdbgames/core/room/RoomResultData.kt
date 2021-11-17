package com.spitzer.igdbgames.core.room

sealed class RoomResultData<out T> {
    data class Success<out T>(val data: T) : RoomResultData<T>()
    data class Error(val failure: Exception) :
        RoomResultData<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[failure=$failure]"
        }
    }
}
