package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GameCover(
    val id: Int,
    val url: String = "",
) : Parcelable
