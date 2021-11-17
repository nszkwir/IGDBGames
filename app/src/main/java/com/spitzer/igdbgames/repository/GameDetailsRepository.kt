package com.spitzer.igdbgames.repository

import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.data.LocalRating

interface GameDetailsRepository {

    suspend fun getGame(
        gameId: Int,
        successFromDatabase: (Game?, LocalRating?) -> Unit,
        error: () -> Unit
    )
}
