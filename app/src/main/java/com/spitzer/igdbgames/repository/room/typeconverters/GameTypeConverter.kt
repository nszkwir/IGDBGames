package com.spitzer.igdbgames.repository.room.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.spitzer.igdbgames.repository.data.GameCover
import com.spitzer.igdbgames.repository.data.GameGenre
import com.spitzer.igdbgames.repository.data.GamePlatform
import com.spitzer.igdbgames.repository.data.GameScreenshot

class GameTypeConverter {

    @TypeConverter
    fun gameCoverToJson(value: GameCover?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameCover(value: String?): GameCover? {
        return if (value == null) {
            null
        } else {
            Gson().fromJson(value, GameCover::class.java)
        }
    }

    @TypeConverter
    fun gameCoverListToJson(value: List<GameCover>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameCoverList(value: String?): List<GameCover> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameCover>::class.java).toList()
        }
    }

    @TypeConverter
    fun gameGenreListToJson(value: List<GameGenre>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameGenreList(value: String?): List<GameGenre> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameGenre>::class.java).toList()
        }
    }

    @TypeConverter
    fun gamePlatformListToJson(value: List<GamePlatform>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGamePlatformList(value: String?): List<GamePlatform> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GamePlatform>::class.java).toList()
        }
    }

    @TypeConverter
    fun gameScreenshotListToJson(value: List<GameScreenshot>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameScreenshotList(value: String?): List<GameScreenshot> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameScreenshot>::class.java).toList()
        }
    }
}
