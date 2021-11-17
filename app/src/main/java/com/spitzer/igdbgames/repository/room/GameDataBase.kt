package com.spitzer.igdbgames.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spitzer.igdbgames.repository.room.data.GameRoomDto
import com.spitzer.igdbgames.repository.room.data.LocalRatingRoomDto
import com.spitzer.igdbgames.repository.room.typeconverters.GameTypeConverter

@Database(
    entities = [GameRoomDto::class, LocalRatingRoomDto::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GameTypeConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gamesDao(): GameDao
    abstract fun localRatingDao(): LocalRatingDao
}
