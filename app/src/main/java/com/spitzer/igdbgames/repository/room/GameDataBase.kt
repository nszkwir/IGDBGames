package com.spitzer.igdbgames.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.room.typeconverters.GameTypeConverter

@Database(
    entities = [Game::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GameTypeConverter::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gamesDao(): GameDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
