package com.evaluation.githubrepository.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?,
)
