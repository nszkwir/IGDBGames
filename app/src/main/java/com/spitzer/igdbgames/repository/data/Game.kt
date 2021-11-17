package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Parcelize
data class Game(
    val id: Int,
    val cover: GameCover? = GameCover(-1, ""),
    val genres: List<GameGenre>? = listOf(),
    val name: String? = "",
    val platforms: List<GamePlatform>? = listOf(),
    val storyline: String? = "",
    val summary: String? = "",
    val url: String? = "",
    val releaseDate: Long? = 0,
    val rating: Double? = 0.0,
    val ratingCount: Int? = 0,
    val screenshots: List<GameScreenshot>? = listOf()
) : Parcelable

fun Game.getPlatformsNames(): String {
    var stringResult = ""
    if (this.platforms != null && this.platforms.isNotEmpty()) {
        stringResult = this.platforms[0].name
        if (this.platforms.size > 1) {
            for (i in 1 until this.platforms.size) {
                stringResult += ", ${this.platforms[i].name}"
            }
        }
    }
    return stringResult
}

fun Game.getReleaseDate(): String {
    val dateFromTimeStamp = DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(this.releaseDate ?: 0))
    return dateFromTimeStamp.subSequence(0, 4).toString()
}
