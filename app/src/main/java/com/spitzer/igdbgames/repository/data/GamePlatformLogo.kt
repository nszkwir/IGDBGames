package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GamePlatformLogo(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("url") val url: String = "",
) : Parcelable
