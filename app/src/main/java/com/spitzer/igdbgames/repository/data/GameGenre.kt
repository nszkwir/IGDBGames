package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GameGenre(
    val id: Int,
    val name: String = "",
) : Parcelable
