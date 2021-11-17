package com.spitzer.igdbgames.repository.retrofit

import com.spitzer.igdbgames.repository.retrofit.data.GameDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface GamesService {
    @POST("games")
    suspend fun getGames(
        @Query("fields") fields: String
    ): Response<ArrayList<GameDto>>
}
