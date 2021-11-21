package com.spitzer.igdbgames.ui.main

sealed class MainViewState {

    data class LoadingInitialGameList(
        val isSuccess: Boolean,
        val isLastPage: Boolean
    ) : MainViewState()

    data class RefreshingGameList(
        val isSuccess: Boolean,
        val isLastPage: Boolean
    ) : MainViewState()

    data class AddNextGamePageToList(
        val isSuccess: Boolean,
        val isLastPage: Boolean,
        val startingPosition: Int,
        val gamesAmountAdded: Int
    ) : MainViewState()

    data class AddedAllGamesFromDb(
        val isSuccess: Boolean,
        val isLastPage: Boolean
    ) : MainViewState()
}
