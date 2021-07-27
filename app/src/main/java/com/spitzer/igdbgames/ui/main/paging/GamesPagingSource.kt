package com.spitzer.igdbgames.ui.main.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.retrofit.GamesRepository
import com.spitzer.igdbgames.repository.retrofit.GamesRepositoryImpl


/** Unused class
 *  GamesPagingSource was first approach to Paging3 implementation
 *  Once local db support was introduced, was replaced by GameRemoteMediator
 */
class GamesPagingSource(private val repository: GamesRepository) :
    PagingSource<Int, Game>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val pageNumber = params.key ?: 1
        return try {
            val response = repository.getGames(pageNumber)
            var resultData: ArrayList<Game>? = null
            var nextPageNumber: Int? = null
            if (response is ResultData.Success &&
                response.data!!.size == GamesRepositoryImpl.GAME_FETCH_NUMBER
            ) {
                nextPageNumber = pageNumber + 1
                resultData = response.data
            }

            LoadResult.Page(
                data = resultData!!.toList(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
