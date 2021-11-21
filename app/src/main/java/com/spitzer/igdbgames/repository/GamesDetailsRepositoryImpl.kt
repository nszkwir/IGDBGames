package com.spitzer.igdbgames.repository

import com.spitzer.igdbgames.core.room.RoomResultData
import com.spitzer.igdbgames.core.room.localSafeCall
import com.spitzer.igdbgames.core.room.localSafeInsert
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.data.LocalRating
import com.spitzer.igdbgames.repository.room.GameDatabase
import com.spitzer.igdbgames.repository.room.data.LocalRatingRoomDto
import com.spitzer.igdbgames.repository.room.data.parseToGame
import com.spitzer.igdbgames.repository.room.data.parseToLocalRating
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GamesDetailsRepositoryImpl @Inject constructor(
    private val dataBase: GameDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GameDetailsRepository {

    override suspend fun getGame(
        gameId: Int,
        successFromDatabase: (Game?, LocalRating?) -> Unit,
        error: () -> Unit
    ) {
        withContext(dispatcher) {
            val gameData = localSafeCall {
                dataBase.gamesDao().getGameById(gameId)
            }
            when (gameData) {
                is RoomResultData.Success -> {
                    val gameFromDatabase: Game? = gameData.data?.parseToGame()
                    val localRatingData = localSafeCall {
                        dataBase.localRatingDao().getGameRating(gameId)
                    }
                    when (localRatingData) {
                        is RoomResultData.Success -> {
                            val localRatingFromDatabase: LocalRating? =
                                localRatingData.data?.parseToLocalRating()
                            successFromDatabase(gameFromDatabase, localRatingFromDatabase)
                        }
                        is RoomResultData.Error -> {
                            successFromDatabase(gameFromDatabase, null)
                        }
                    }
                }
                is RoomResultData.Error -> {
                    error()
                }
            }
        }
    }

    override suspend fun udpateLocalRating(
        gameId: Int,
        localRating: Double,
        success: () -> Unit,
        error: () -> Unit
    ) {
        withContext(dispatcher) {
            val savedData = localSafeInsert {
                dataBase.localRatingDao().insert(
                    LocalRatingRoomDto(gameId, localRating)
                )
            }
            when (savedData) {
                is RoomResultData.Success -> {
                    success()
                }
                is RoomResultData.Error -> {
                    error()
                }
            }
        }
    }
}
