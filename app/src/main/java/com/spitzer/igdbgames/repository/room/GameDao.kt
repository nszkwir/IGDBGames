package com.spitzer.igdbgames.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spitzer.igdbgames.repository.room.data.GameRoomDto

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameRoomDto>)

    @Query("SELECT * FROM games ORDER BY id ASC")
    fun allGames(): List<GameRoomDto>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
