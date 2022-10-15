package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
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
    override suspend fun getJobs(): FetchedJobs {
        val executorId = authRepository.getLocalExecutorId()!!

        try {
            with(jobsApi.executorJobs(executorId.toString()).awaitResponse()) {
                return when (code()) {
                    200 -> {
                        if (cache.getById(executorId) == null) {
                            cache.insert(
                                PolusCacheListing(executorId, body()!!, System.currentTimeMillis()/1000)
                            )
                        } else {
                            cache.updateCache(PolusCacheListing(executorId, body()!!, System.currentTimeMillis()/1000))
                        }
                        FetchedJobs.SuccessJobFetch(body()!!)
                    }
                    else -> cache.getById(executorId)?.let {
                        FetchedJobs.CachedJobs(it.jobs, it.cacheDate, HttpException(this))
                    } ?: FetchedJobs.CachedJobs(listOf(), System.currentTimeMillis()/1000, HttpException(this))
                }
            }
        } catch (e: Exception) {
            if (e is java.net.ConnectException || e is java.net.SocketTimeoutException) {
                return cache.getById(executorId)?.let {
                    FetchedJobs.CachedJobs(it.jobs, it.cacheDate, e)
                } ?: FetchedJobs.CachedJobs(listOf(), System.currentTimeMillis()/1000, e)
            }
            throw e
        }
    }

    override suspend fun updateJobStatus(jobEntity: JobEntity) {
        // Обновить локальную
        // Запланировать запрос
        val executorId = authRepository.getLocalExecutorId()!!
        with(jobsApi.updateJob(jobEntity).awaitResponse()) {
            when (code()) {
                200 -> {}
                else -> throw HttpException(this)
            }
        }
    }
}