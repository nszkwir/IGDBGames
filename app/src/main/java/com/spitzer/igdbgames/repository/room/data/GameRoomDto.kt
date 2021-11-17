package com.spitzer.igdbgames.repository.room.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.spitzer.igdbgames.repository.data.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "games")
@Parcelize
data class GameRoomDto(
    @PrimaryKey @field:SerializedName("id") val id: Int,
    @field:SerializedName("cover") val cover: GameCoverRoomDto? = GameCoverRoomDto(-1, ""),
    @field:SerializedName("genres") val genres: List<GameGenreRoomDto>? = listOf(),
    @field:SerializedName("name") val name: String? = "",
    @field:SerializedName("platforms") val platforms: List<GamePlatformRoomDto>? = listOf(),
    @field:SerializedName("storyline") val storyline: String? = "",
    @field:SerializedName("summary") val summary: String? = "",
    @field:SerializedName("url") val url: String? = "",
    @field:SerializedName("first_release_date") val releaseDate: Long? = 0,
    @field:SerializedName("total_rating") val rating: Double? = 0.0,
    @field:SerializedName("total_rating_count") val ratingCount: Int? = 0,
    @field:SerializedName("screenshots") val screenshots: List<GameScreenshotRoomDto>? = listOf()
) : Parcelable

fun GameRoomDto.parseToGame(): Game {
    return Game(
        id = this.id,
        cover = GameCover(this.cover?.id ?: 0, this.cover?.url ?: ""),
        genres = this.genres?.map { g -> GameGenre(g.id, g.name) },
        name = this.name,
        platforms = this.platforms?.map { p ->
            GamePlatform(
                p.id,
                p.name,
                GamePlatformLogo(
                    p.platform_logo?.id ?: 0,
                    p.platform_logo?.url ?: ""
                )
            )
        },
        storyline = this.storyline,
        summary = this.summary,
        url = this.url,
        releaseDate = this.releaseDate,
        rating = this.rating,
        ratingCount = this.ratingCount,
        screenshots = this.screenshots?.map { s -> GameScreenshot(s.id, s.url) }
    )
}