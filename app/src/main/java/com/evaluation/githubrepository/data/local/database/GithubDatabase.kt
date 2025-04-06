package com.evaluation.githubrepository.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evaluation.githubrepository.data.local.dao.CacheMetadataDao
import com.evaluation.githubrepository.data.local.dao.RemoteKeysDao
import com.evaluation.githubrepository.data.local.dao.RepoDetailsDao
import com.evaluation.githubrepository.data.local.dao.ReposDao
import com.evaluation.githubrepository.data.local.entity.CacheMetadataEntity
import com.evaluation.githubrepository.data.local.entity.RemoteKeysEntity
import com.evaluation.githubrepository.data.local.entity.RepoDetailsEntity
import com.evaluation.githubrepository.data.local.entity.ReposEntity

@Database(
    entities = [ReposEntity::class, RemoteKeysEntity::class, CacheMetadataEntity::class, RepoDetailsEntity::class],
    version = 1,
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun remoteKeysDao(): RemoteKeysDao

    abstract fun reposDao(): ReposDao

    abstract fun repoDetailsDao(): RepoDetailsDao

    abstract fun cacheMetadataDao(): CacheMetadataDao

    suspend fun lastUpdated(): Long = cacheMetadataDao().getLastUpdated() ?: 0L
}
