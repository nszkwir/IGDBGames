package com.spitzer.igdbgames.repository.retrofit

import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.repository.data.Game

interface GamesRepository {
    suspend fun getGames(currentPage: Int): ResultData<ArrayList<Game>?>
}