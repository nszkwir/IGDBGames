package com.spitzer.igdbgames.repository.room.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GameVideoRoomDto(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("video_id") val videoId: String = ""
) : Parcelable
