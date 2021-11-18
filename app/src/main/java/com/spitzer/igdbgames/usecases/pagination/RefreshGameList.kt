package com.spitzer.igdbgames.usecases.pagination

import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.ui.pagination.PaginationResult

interface RefreshGameList {
    suspend fun refreshGameList() : PaginationResult<ArrayList<Game>>
}