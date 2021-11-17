package com.spitzer.igdbgames.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spitzer.igdbgames.repository.data.Game

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)

    @Query("SELECT * FROM games ORDER BY id ASC")
    fun allGames(): List<Game>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
