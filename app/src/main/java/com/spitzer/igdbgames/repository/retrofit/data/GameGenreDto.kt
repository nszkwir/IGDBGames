package com.spitzer.igdbgames.repository.retrofit.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GameGenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String = "",
) : Parcelable
