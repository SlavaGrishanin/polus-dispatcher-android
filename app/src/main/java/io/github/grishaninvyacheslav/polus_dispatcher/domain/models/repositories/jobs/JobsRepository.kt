package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources.IJobsDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room.PolusCacheDao
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room.PolusCacheListing
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository

class JobsRepository(
    private val jobsApi: IJobsDataSource,
    private val authRepository: IAuthRepository,
    private val cache: PolusCacheDao
) : IJobsRepository {
    override suspend fun getJobs(): List<JobEntity> {
        return jobsApi.executorJobs(authRepository.getLocalExecutorId()!!).apply {
            //cache.insert(PolusCacheListing(authRepository.getLocalExecutorId()!!, this))
        }
    }

    override suspend fun wereUpdated(): Boolean {
        return jobsApi.isExecutorJobsUpdated(authRepository.getLocalExecutorId()!!)
    }

    override suspend fun updateJob(job: JobEntity) {
        // TODO("Not yet implemented")
    }
}