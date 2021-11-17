package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class LocalRating(
    val gameId: Int,
    val rating: Double? = 0.0
) : Parcelable
