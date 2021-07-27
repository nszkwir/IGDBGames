package com.spitzer.igdbgames.ui.main.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.retrofit.GamesRepository
import com.spitzer.igdbgames.repository.room.GameDatabase
import com.spitzer.igdbgames.repository.room.RemoteKeys
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class GameRemoteMediator(
    private val repository: GamesRepository,
    private val gameDatabase: GameDatabase
) : RemoteMediator<Int, Game>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Game>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val response = repository.getGames(page)
            var resultData: ArrayList<Game>? = null
            if (response is ResultData.Success) {
                resultData = response.data
                val endOfPaginationReached = resultData.isNullOrEmpty()

                gameDatabase.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        gameDatabase.remoteKeysDao().clearRemoteKeys()
                        gameDatabase.gamesDao().clearGames()
                    }
                    val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = resultData?.map {
                        RemoteKeys(gameId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    gameDatabase.remoteKeysDao().insertAll(keys ?: listOf())
                    gameDatabase.gamesDao().insertAll(resultData?.toList() ?: listOf())
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                return MediatorResult.Error(Exception("error fetching data"))
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Game>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { game ->
                gameDatabase.remoteKeysDao().remoteKeysGameId(game.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Game>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { game ->
                gameDatabase.remoteKeysDao().remoteKeysGameId(game.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Game>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { gameId ->
                gameDatabase.remoteKeysDao().remoteKeysGameId(gameId)
            }
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
