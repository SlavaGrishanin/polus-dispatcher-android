package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

interface IJobsRepository {
    suspend fun getJobs(): List<JobEntity>
    suspend fun wereUpdated(): Boolean
}