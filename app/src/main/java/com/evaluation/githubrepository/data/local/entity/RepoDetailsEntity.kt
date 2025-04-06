package com.evaluation.githubrepository.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repository_details")
data class RepoDetailsEntity(
    @PrimaryKey val id: Int,
    val lastFetchedAt: Long,
    val description: String? = null,
    val language: String? = null,
    val name: String? = null,
    val owner: String? = null,
    val stargazersCount: Int? = null,
)
