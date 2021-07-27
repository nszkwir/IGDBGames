package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class GameGenre(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String = "",
) : Parcelable
