package com.example.responsiveapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_queries")
data class SearchQueryEntity(
    @PrimaryKey
    val query: String,
    val totalResults: Int,
    val cachedCount: Int,
    val isComplete: Boolean,
    val lastFetchedAt: Long = System.currentTimeMillis(),
)