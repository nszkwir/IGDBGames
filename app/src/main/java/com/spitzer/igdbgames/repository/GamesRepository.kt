package com.spitzer.igdbgames.repository

import com.spitzer.igdbgames.repository.data.Game

interface GamesRepository {

    suspend fun loadRefreshGames(
        successFromNetwork: (Boolean, ArrayList<Game>) -> Unit,
        successFromDatabase: (ArrayList<Game>) -> Unit,
        error: () -> Unit
    )

    suspend fun loadNextPage(
        currentPage: Int,
        successFromNetwork: (Boolean, ArrayList<Game>) -> Unit,
        successFromDatabase: (ArrayList<Game>) -> Unit,
        error: () -> Unit
    )
}
