package com.spitzer.igdbgames.repository.retrofit

import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.core.safeCall
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.room.GameDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GamesRepositoryImpl @Inject constructor(
    private val service: GamesService,
    private val dataBase: GameDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GamesRepository {

    override suspend fun getGames(currentPage: Int, gameFetchNumber: Int): ResultData<ArrayList<Game>?> {
        return withContext(dispatcher) {
            return@withContext safeCall {
                service.getGames(
                    getGamesQuery(currentPage, gameFetchNumber)
                )
            }
        }
    }

    companion object {

        private fun calculateOffset(currentPage: Int, gameFetchNumber: Int): Int {
            return (currentPage - 1) * gameFetchNumber
        }

        fun getGamesQuery(currentPage: Int, gameFetchNumber: Int): String {
            val offset = calculateOffset(currentPage,gameFetchNumber)
            return "id, name, first_release_date,summary, storyline, cover.url, platforms.name," +
                    "platforms.platform_logo.url, genres.name,rating, rating_count, total_rating," +
                    "total_rating_count,url,screenshots.url;limit $gameFetchNumber; offset $offset; sort id asc;"
        }
    }
}
