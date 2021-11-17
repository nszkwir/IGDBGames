package com.spitzer.igdbgames.repository.retrofit.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Parcelize
data class GameDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cover") val cover: GameCoverDto? = GameCoverDto(-1, ""),
    @SerializedName("genres") val genres: List<GameGenreDto>? = listOf(),
    @SerializedName("name") val name: String? = "",
    @SerializedName("platforms") val platforms: List<GamePlatformDto>? = listOf(),
    @SerializedName("storyline") val storyline: String? = "",
    @SerializedName("summary") val summary: String? = "",
    @SerializedName("url") val url: String? = "",
    @SerializedName("first_release_date") val releaseDate: Long? = 0,
    @SerializedName("total_rating") val rating: Double? = 0.0,
    @SerializedName("total_rating_count") val ratingCount: Int? = 0,
    @SerializedName("screenshots") val screenshots: List<GameScreenshotDto>? = listOf()
) : Parcelable

fun GameDto.getPlatformsNames(): String {
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

fun GameDto.getReleaseDate(): String {
    val dateFromTimeStamp = DateTimeFormatter.ISO_INSTANT
        .format(java.time.Instant.ofEpochSecond(this.releaseDate ?: 0))
    return dateFromTimeStamp.subSequence(0, 4).toString()
}
