@file:JvmName("UserProfileMapperKt")

package com.example.responsiveapp.data.mapper
import com.example.responsiveapp.data.local.entity.UserProfileEntity
import com.example.responsiveapp.data.remote.dto.firebase.UserProfileDto
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.SyncStatus
import com.example.responsiveapp.domain.model.UserProfile

fun UserProfileEntity.toDomain(): UserProfile =
    UserProfile(
        id = id,
        name = name,
        gender = Gender.valueOf(gender),
        age = age,
        height = height,
        weight = weight,
        targetWeight = targetWeight,
        activityLevel = ActivityLevel.valueOf(activityLevel),
        goal = Goal.valueOf(goal),
        updatedAt = updatedAt
    )

fun UserProfile.toEntity(
    syncStatus: SyncStatus = SyncStatus.PENDING,
    retryCount: Int = 0,
    lastSyncAttempt: Long? = null,
): UserProfileEntity =
    UserProfileEntity(
        id = id,
        name = name,
        gender = gender.name,
        age = age,
        height = height,
        weight = weight,
        targetWeight = targetWeight,
        activityLevel = activityLevel.name,
        goal = goal.name,
        updatedAt = updatedAt,
        syncStatus = syncStatus,
        retryCount = retryCount,
        lastSyncAttempt = lastSyncAttempt
    )

fun UserProfileDto.toDomain(id: String): UserProfile? {
    if (
        gender == null || age == null || height == null ||
        weight == null || activityLevel == null || goal == null
    ) return null

    return UserProfile(
        id = id,
        name = name.orEmpty(),
        gender = Gender.valueOf(gender),
        age = age,
        height = height,
        weight = weight,
        targetWeight = targetWeight ?: 62.8f,
        activityLevel = ActivityLevel.valueOf(activityLevel),
        goal = Goal.valueOf(goal),
        updatedAt = updatedAt ?: System.currentTimeMillis()
    )
}

fun UserProfile.toDto(): UserProfileDto =
    UserProfileDto(
        name = name,
        gender = gender.name,
        age = age,
        height = height,
        weight = weight,
        targetWeight = targetWeight,
        activityLevel = activityLevel.name,
        goal = goal.name,
        updatedAt = updatedAt
    )

fun UserProfileEntity.toFirestoreDto(): UserProfileDto =
    UserProfileDto(
        name = name,
        gender = gender,
        age = age,
        height = height,
        weight = weight,
        targetWeight = targetWeight,
        activityLevel = activityLevel,
        goal = goal,
        updatedAt = updatedAt
    )
