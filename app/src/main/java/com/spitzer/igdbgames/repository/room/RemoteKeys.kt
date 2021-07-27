package com.spitzer.igdbgames.repository.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val gameId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
