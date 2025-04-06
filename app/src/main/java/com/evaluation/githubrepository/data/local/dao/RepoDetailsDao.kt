package com.evaluation.githubrepository.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.evaluation.githubrepository.data.local.entity.RepoDetailsEntity

@Dao
interface RepoDetailsDao {
    @Upsert
    suspend fun insertRepo(repo: RepoDetailsEntity)

    @Query("SELECT * FROM repository_details WHERE owner = :owner AND name = :repo")
    suspend fun getRepo(
        owner: String,
        repo: String,
    ): RepoDetailsEntity?
}
