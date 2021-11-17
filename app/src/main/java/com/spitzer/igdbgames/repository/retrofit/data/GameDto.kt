package com.spitzer.igdbgames.repository.retrofit.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.spitzer.igdbgames.repository.data.*
import com.spitzer.igdbgames.repository.room.data.*
import kotlinx.parcelize.Parcelize

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
    @SerializedName("screenshots") val screenshots: List<GameScreenshotDto>? = listOf(),
    @SerializedName("videos") val videos: List<GameVideoDto>? = listOf()
) : Parcelable

fun GameDto.parseToGame(): Game {
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
        screenshots = this.screenshots?.map { s -> GameScreenshot(s.id, s.url) },
        videos = this.videos?.map { v -> GameVideo(v.id, v.name, v.videoId) }
    )
}

fun GameDto.parseToGameRoomDto(): GameRoomDto {
    return GameRoomDto(
        id = this.id,
        cover = GameCoverRoomDto(this.cover?.id ?: 0, this.cover?.url ?: ""),
        genres = this.genres?.map { g -> GameGenreRoomDto(g.id, g.name) },
        name = this.name,
        platforms = this.platforms?.map { p ->
            GamePlatformRoomDto(
                p.id,
                p.name,
                GamePlatformLogoRoomDto(
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
        screenshots = this.screenshots?.map { s -> GameScreenshotRoomDto(s.id, s.url) },
        videos = this.videos?.map { v -> GameVideoRoomDto(v.id, v.name, v.videoId) }
    )
}