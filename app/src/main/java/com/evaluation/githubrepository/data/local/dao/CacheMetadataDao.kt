package com.evaluation.githubrepository.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.githubrepository.data.local.entity.CacheMetadataEntity

@Dao
interface CacheMetadataDao {
    @Upsert
    suspend fun insert(metadata: CacheMetadataEntity)

    @Query("SELECT lastUpdated FROM cache_metadata WHERE id = 1")
    suspend fun getLastUpdated(): Long?

    @Query("DELETE FROM cache_metadata")
    suspend fun clear()
}
