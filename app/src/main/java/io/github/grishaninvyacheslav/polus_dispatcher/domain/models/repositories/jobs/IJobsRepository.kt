package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import retrofit2.HttpException
import java.lang.Exception

interface IJobsRepository {
    suspend fun getJobs(): FetchedJobs
    suspend fun updateJobStatus(jobEntity: JobEntity)
}

sealed class FetchedJobs {
    data class SuccessJobFetch(val jobs: List<JobExpandedEntity>): FetchedJobs()
    data class CachedJobs(val jobs: List<JobExpandedEntity>, val cacheDate: Long, val exception: Exception): FetchedJobs()
}