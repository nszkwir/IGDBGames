package com.spitzer.igdbgames.repository

sealed class RepositoryResultData<out T> {
    data class SuccessFromNetwork<out T>(val data: T, val isLastPage: Boolean) :
        RepositoryResultData<T>()

    data class SuccessFromDataBase<out T>(val data: T) :
        RepositoryResultData<T>()

    data class Error(val failure: String, val code: Int = DEFAULT_ERROR_CODE) :
        RepositoryResultData<Nothing>()

    companion object {
        const val DEFAULT_ERROR_CODE = -999
    }
}
