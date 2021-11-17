package com.spitzer.igdbgames.repository

import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.core.network.safeCall
import com.spitzer.igdbgames.core.room.RoomResultData
import com.spitzer.igdbgames.core.room.localSafeCall
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.retrofit.GamesRepository
import com.spitzer.igdbgames.repository.retrofit.GamesService
import com.spitzer.igdbgames.repository.retrofit.data.parseToGame
import com.spitzer.igdbgames.repository.retrofit.data.parseToGameRoomDto
import com.spitzer.igdbgames.repository.room.GameDatabase
import com.spitzer.igdbgames.repository.room.data.parseToGame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val service: GamesService,
    private val dataBase: GameDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GamesRepository {

    override suspend fun loadRefreshGames(
        successFromNetwork: (Boolean, ArrayList<Game>) -> Unit,
        successFromDatabase: (ArrayList<Game>) -> Unit,
        error: () -> Unit
    ) {
        return withContext(dispatcher) {
            val result = safeCall {
                service.getGames(
                    getGamesQuery(1)
                )
            }
            when (result) {
                is ResultData.Success -> {
                    val resultFromNetwork: ArrayList<Game> = result.data?.map {
                        it.parseToGame()
                    } as ArrayList<Game>
                    result.data.let {
                        dataBase.gamesDao().insertAll(
                            result.data.map {
                                it.parseToGameRoomDto()
                            }
                        )
                    }
                    successFromNetwork(
                        resultFromNetwork.size < GAME_FETCH_NUMBER,
                        resultFromNetwork
                    )
                }
                is ResultData.Error -> {
                    val dbResults = localSafeCall {
                        dataBase.gamesDao().allGames()
                    }
                    when (dbResults) {

                        is RoomResultData.Success -> {
                            val resultFromDatabase: ArrayList<Game> = dbResults.data?.map {
                                it.parseToGame()
                            } as ArrayList<Game>

                            successFromDatabase(resultFromDatabase)
                        }
                        is RoomResultData.Error -> {
                            error()
                        }
                    }
                }
            }
        }
    }

    override suspend fun loadNextPage(
        currentPage: Int,
        successFromNetwork: (Boolean, ArrayList<Game>) -> Unit,
        successFromDatabase: (ArrayList<Game>) -> Unit,
        error: () -> Unit
    ) {
        return withContext(dispatcher) {
            val result = safeCall {
                service.getGames(
                    getGamesQuery(currentPage)
                )
            }
            when (result) {
                is ResultData.Success -> {
                    val resultFromNetwork: ArrayList<Game> = result.data?.map {
                        it.parseToGame()
                    } as ArrayList<Game>
                    result.data.let {
                        dataBase.gamesDao().insertAll(
                            result.data.map {
                                it.parseToGameRoomDto()
                            }
                        )
                    }
                    successFromNetwork(
                        resultFromNetwork.size < GAME_FETCH_NUMBER,
                        resultFromNetwork
                    )
                }
                is ResultData.Error -> {
                    val dbResults = localSafeCall {
                        dataBase.gamesDao().allGames()
                    }
                    when (dbResults) {

                        is RoomResultData.Success -> {
                            val resultFromDatabase: ArrayList<Game> = dbResults.data?.map {
                                it.parseToGame()
                            } as ArrayList<Game>

                            successFromDatabase(resultFromDatabase)
                        }
                        is RoomResultData.Error -> {
                            error()
                        }
                    }
                }
            }
        }
    }

    companion object {

        private const val GAME_FETCH_NUMBER = 50

        fun getGamesQuery(currentPage: Int, gameFetchNumber: Int = GAME_FETCH_NUMBER): String {
            val offset = (currentPage - 1) * gameFetchNumber
            return "id, name, first_release_date,summary, storyline, cover.url, platforms.name," +
                    "platforms.platform_logo.url, genres.name,rating, rating_count, total_rating," +
                    "total_rating_count,url,screenshots.url;" +
                    "limit $gameFetchNumber; offset $offset; sort id asc;"
        }
    }
}
