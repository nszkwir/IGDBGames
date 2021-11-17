package com.spitzer.igdbgames.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spitzer.igdbgames.repository.room.data.LocalRatingRoomDto

@Dao
interface LocalRatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: LocalRatingRoomDto)

    @Query("SELECT * FROM local_rating WHERE gameId = :gameId")
    suspend fun getGameRating(gameId: Int): LocalRatingRoomDto?

    @Query("DELETE FROM local_rating WHERE gameId = :gameId")
    suspend fun deleteGameRating(gameId: Int)
}
