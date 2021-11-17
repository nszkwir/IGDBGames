package com.spitzer.igdbgames.repository.retrofit.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GameCoverDto(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String = "",
) : Parcelable
