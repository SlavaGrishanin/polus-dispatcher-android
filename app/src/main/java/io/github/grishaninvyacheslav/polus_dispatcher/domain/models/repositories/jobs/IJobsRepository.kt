package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity

interface IJobsRepository {
    suspend fun getJobs(): List<JobExpandedEntity>
    suspend fun updateJobStatus(jobEntity: JobEntity)
}