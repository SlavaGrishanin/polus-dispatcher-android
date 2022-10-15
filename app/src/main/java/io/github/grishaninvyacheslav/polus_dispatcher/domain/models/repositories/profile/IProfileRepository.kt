package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.ExecutorEntity

interface IProfileRepository {
    suspend fun getProfile(): ExecutorEntity
    suspend fun updateProfile(profile: ExecutorEntity)
}