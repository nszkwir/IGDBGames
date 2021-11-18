package com.spitzer.igdbgames.repository

import com.spitzer.igdbgames.repository.data.Game

interface GamesRepository {
    suspend fun loadNextPage(currentPage: Int): RepositoryResultData<ArrayList<Game>>
}
