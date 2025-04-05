package com.evaluation.githubrepository.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_metadata")
data class CacheMetadataEntity(
    @PrimaryKey val id: Int = 1,
    val lastUpdated: Long,
)
