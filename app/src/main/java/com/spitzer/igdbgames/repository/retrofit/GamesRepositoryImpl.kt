package com.spitzer.igdbgames.repository.retrofit

import android.content.Context
import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.core.network.safeCall
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.room.GameDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import android.net.ConnectivityManager
import com.spitzer.igdbgames.core.room.RoomResultData
import com.spitzer.igdbgames.core.room.localSafeCall


class GamesRepositoryImpl @Inject constructor(
    private val service: GamesService,
    private val dataBase: GameDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GamesRepository {

    suspend fun loadRefreshGames(currentPage: Int, gameFetchNumber: Int): ResultData<ArrayList<Game>?> {
        return withContext(dispatcher) {
            val result = safeCall {
                service.getGames(
                    getGamesQuery(currentPage, gameFetchNumber)
                )
            }
            when (result) {
                is ResultData.Success -> {

                }
                is ResultData.Error -> {
                    val dbResults = localSafeCall {
                        dataBase.gamesDao().allGames()
                    }
                    when(dbResults) {
                        is RoomResultData.Success -> {

                        }
                        is RoomResultData.Error -> {

                        }
                    }
                }
            }

            return@withContext result
        }
    }

    suspend fun loadNextPage(currentPage: Int, gameFetchNumber: Int): ResultData<ArrayList<Game>?> {
        return withContext(dispatcher) {
            val result = safeCall {
                service.getGames(
                    getGamesQuery(currentPage, gameFetchNumber)
                )
            }
            return@withContext result
        }
    }

    override suspend fun getGames(currentPage: Int, gameFetchNumber: Int): ResultData<ArrayList<Game>?> {
        return withContext(dispatcher) {
            return@withContext safeCall {
                service.getGames(
                    getGamesQuery(currentPage, gameFetchNumber)
                )
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
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
