package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GameVideo(
    val id: Int,
    val name: String = "",
    val videoId: String = ""
) : Parcelable
