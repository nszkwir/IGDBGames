package com.spitzer.igdbgames.ui.main

sealed class MainViewEvent {

    sealed class PaginationEvent : MainViewEvent() {

        data class RefreshGameListEvent(
            private val isRefreshListEvent: Boolean = true
        ) : PaginationEvent()

        data class FetchNextPageEvent(
            private val isFetchNextPageEvent: Boolean = true
        ) : PaginationEvent()

//        data class RetryFetchNextPageEvent(
//            private val isRetryFetchNextPageEvent: Boolean = true
//        ) : PaginationEvent()

    }

    data class GoToGameDetails(val gameId: Int) : MainViewEvent()
}
