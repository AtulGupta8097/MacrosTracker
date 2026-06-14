package com.example.responsiveapp.data.local

import com.example.responsiveapp.data.local.entity.SearchQueryEntity

object CachePolicy {

    private const val SEARCH_CACHE_TTL_MS = 15 * 60 * 1000L

    const val MAX_CACHED_QUERIES = 500

    fun SearchQueryEntity.isCacheFresh(): Boolean =
        System.currentTimeMillis() - lastFetchedAt < SEARCH_CACHE_TTL_MS

    fun SearchQueryEntity.hasSufficientResults(
        requestedLimit: Int,
    ): Boolean = isComplete || cachedCount >= requestedLimit

    fun SearchQueryEntity.canServe(
        requestedLimit: Int,
    ): Boolean = isCacheFresh() && hasSufficientResults(requestedLimit)
}