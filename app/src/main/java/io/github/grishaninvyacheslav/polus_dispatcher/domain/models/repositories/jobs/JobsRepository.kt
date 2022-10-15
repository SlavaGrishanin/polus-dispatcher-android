package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources.IJobsDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room.PolusCacheDao
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.room.PolusCacheListing
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository
import retrofit2.HttpException
import retrofit2.awaitResponse

class JobsRepository(
    private val jobsApi: IJobsDataSource,
    private val authRepository: IAuthRepository,
    private val cache: PolusCacheDao
) : IJobsRepository {
    override suspend fun getJobs(): List<JobExpandedEntity> {
        val executorId = authRepository.getLocalExecutorId()!!
        with(jobsApi.executorJobs(executorId.toString()).awaitResponse()) {
            when (code()) {
                200 -> {
//                    if (cache.getById(executorId) == null) {
//                        cache.insert(
//                            PolusCacheListing(executorId, body()!!)
//                        )
//                    } else {
//                        cache.updateCache(PolusCacheListing(executorId, body()!!))
//                    }
                    return body()!!
                }
                else -> throw HttpException(this)
            }
        }
    }

    override suspend fun updateJobStatus(jobEntity: JobEntity) {
        val executorId = authRepository.getLocalExecutorId()!!
        with(jobsApi.updateJob(jobEntity).awaitResponse()) {
            when (code()) {
                200 -> { }
                else -> throw HttpException(this)
            }
        }
    }
}