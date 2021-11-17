package com.spitzer.igdbgames.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GamePlatform(
    val id: Int,
    val name: String = "",
    val platform_logo: GamePlatformLogo?
) : Parcelable
