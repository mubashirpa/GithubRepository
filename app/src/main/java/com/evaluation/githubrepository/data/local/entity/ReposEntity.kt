package com.evaluation.githubrepository.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class ReposEntity(
    @PrimaryKey val id: Int,
    val description: String? = null,
    val language: String? = null,
    val name: String? = null,
    val owner: String? = null,
    val stargazersCount: Int? = null,
)
