package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.ExecutorEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources.IExecutorDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository

class ProfileRepository(
    private val executorApi: IExecutorDataSource,
    private val authRepository: IAuthRepository
) : IProfileRepository {
    override suspend fun getProfile(): ExecutorEntity {
        return executorApi.getExecutor(authRepository.getLocalExecutorId()!!)
    }

    override suspend fun updateProfile(profile: ExecutorEntity) {
        return executorApi.updateExecutor(authRepository.getLocalExecutorId()!!, profile)
    }
}