package com.spitzer.igdbgames.repository.room.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.spitzer.igdbgames.repository.data.LocalRating
import kotlinx.parcelize.Parcelize

@Entity(tableName = "local_rating")
@Parcelize
class LocalRatingRoomDto(
    @PrimaryKey
    @field:SerializedName("gameId") val gameId: Int,
    @field:SerializedName("rating") val rating: Double? = 0.0
) : Parcelable

fun LocalRatingRoomDto.parseToLocalRating(): LocalRating {
    return LocalRating(this.gameId, this.rating)
}