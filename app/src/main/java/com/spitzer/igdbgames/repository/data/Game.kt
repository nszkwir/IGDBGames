package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Entity(tableName = "games")
@Parcelize
data class Game(
    @PrimaryKey @field:SerializedName("id") val id: Int,
    @field:SerializedName("cover") val cover: GameCover? = GameCover(-1, ""),
    @field:SerializedName("genres") val genres: List<GameGenre>? = listOf(),
    @field:SerializedName("name") val name: String? = "",
    @field:SerializedName("platforms") val platforms: List<GamePlatform>? = listOf(),
    @field:SerializedName("storyline") val storyline: String? = "",
    @field:SerializedName("summary") val summary: String? = "",
    @field:SerializedName("url") val url: String? = "",
    @field:SerializedName("first_release_date") val releaseDate: Long? = 0,
    @field:SerializedName("total_rating") val rating: Double? = 0.0,
    @field:SerializedName("total_rating_count") val ratingCount: Int? = 0,
    @field:SerializedName("screenshots") val screenshots: List<GameScreenshot>? = listOf()
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
