package com.spitzer.igdbgames.ui.pagination

sealed class PaginationResult<out T> {
    data class Success<out T>(val data: T, val paginationInfo: PaginationInfo) :
        PaginationResult<T>()

    data class Error(val failure: String, val code: Int = DEFAULT_ERROR_CODE) :
        PaginationResult<Nothing>()

    companion object {
        const val DEFAULT_ERROR_CODE = -999
    }
}
