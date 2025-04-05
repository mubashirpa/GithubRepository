package com.evaluation.githubrepository.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.githubrepository.data.local.entity.RemoteKeysEntity

@Dao
interface RemoteKeysDao {
    @Upsert
    suspend fun insertAll(remoteKeys: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeysId(id: Int): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}
