package com.spitzer.igdbgames.ui.pagination

import com.spitzer.igdbgames.repository.GamesRepository
import com.spitzer.igdbgames.repository.RepositoryResultData
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.usecases.pagination.FetchNextGamePage
import javax.inject.Inject

class PaginationUseCases @Inject constructor(
    val repository: GamesRepository
) : FetchNextGamePage {

    override suspend fun fetchNextPage(pageToFetch: Int): PaginationResult<ArrayList<Game>> {
        val repositoryResult = repository.loadNextPage(pageToFetch)
        when (repositoryResult) {
            is RepositoryResultData.SuccessFromNetwork -> {
                return PaginationResult.Success(
                    repositoryResult.data,
                    PaginationInfo(
                        repositoryResult.isLastPage,
                        pageToFetch + 1,
                        false
                    )
                )
            }
            is RepositoryResultData.SuccessFromDataBase -> {
                return PaginationResult.Success(
                    repositoryResult.data,
                    PaginationInfo(
                        true,
                        pageToFetch + 1,
                        true
                    )
                )
            }
            is RepositoryResultData.Error -> {
                return PaginationResult.Error(
                    repositoryResult.failure,
                    repositoryResult.code
                )
            }
        }
    }
}
