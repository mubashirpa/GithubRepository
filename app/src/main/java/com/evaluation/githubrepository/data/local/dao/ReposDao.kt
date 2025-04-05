package com.evaluation.githubrepository.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.githubrepository.data.local.entity.ReposEntity

@Dao
interface ReposDao {
    @Upsert
    suspend fun insertAll(repos: List<ReposEntity>)

    @Query("SELECT * FROM repositories ORDER BY name ASC")
    fun pagingSource(): PagingSource<Int, ReposEntity>

    @Query("SELECT * FROM repositories WHERE owner = :owner AND name = :repo")
    suspend fun getRepo(
        owner: String,
        repo: String,
    ): ReposEntity?

    @Query("DELETE FROM repositories")
    suspend fun clearAll()
}
