package com.spitzer.igdbgames.repository.retrofit.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GamePlatformDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String = "",
    @SerializedName("platform_logo") val platform_logo: GamePlatformLogoDto?
) : Parcelable
