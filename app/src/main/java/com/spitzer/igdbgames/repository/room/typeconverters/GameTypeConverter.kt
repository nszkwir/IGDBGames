package com.spitzer.igdbgames.repository.room.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.spitzer.igdbgames.repository.room.data.GameCoverRoomDto
import com.spitzer.igdbgames.repository.room.data.GameGenreRoomDto
import com.spitzer.igdbgames.repository.room.data.GamePlatformRoomDto
import com.spitzer.igdbgames.repository.room.data.GameScreenshotRoomDto

class GameTypeConverter {

    @TypeConverter
    fun gameCoverToJson(value: GameCoverRoomDto?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameCover(value: String?): GameCoverRoomDto? {
        return if (value == null) {
            null
        } else {
            Gson().fromJson(value, GameCoverRoomDto::class.java)
        }
    }

    @TypeConverter
    fun gameCoverListToJson(value: List<GameCoverRoomDto>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameCoverList(value: String?): List<GameCoverRoomDto> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameCoverRoomDto>::class.java).toList()
        }
    }

    @TypeConverter
    fun gameGenreListToJson(value: List<GameGenreRoomDto>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameGenreList(value: String?): List<GameGenreRoomDto> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameGenreRoomDto>::class.java).toList()
        }
    }

    @TypeConverter
    fun gamePlatformListToJson(value: List<GamePlatformRoomDto>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGamePlatformList(value: String?): List<GamePlatformRoomDto> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GamePlatformRoomDto>::class.java).toList()
        }
    }

    @TypeConverter
    fun gameScreenshotListToJson(value: List<GameScreenshotRoomDto>?): String? {
        return if (value == null) {
            null
        } else {
            Gson().toJson(value)
        }
    }

    @TypeConverter
    fun jsonToGameScreenshotList(value: String?): List<GameScreenshotRoomDto> {
        return if (value == null) {
            listOf()
        } else {
            Gson().fromJson(value, Array<GameScreenshotRoomDto>::class.java).toList()
        }
    }
}
