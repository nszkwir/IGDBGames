package com.spitzer.igdbgames.repository.room.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GamePlatformRoomDto(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("platform_logo") val platform_logo: GamePlatformLogoRoomDto?
) : Parcelable
