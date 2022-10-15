package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.FetchedJobs
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.IJobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile.IProfileRepository
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import io.github.grishaninvyacheslav.polus_dispatcher.utils.getNearest
import io.github.grishaninvyacheslav.polus_dispatcher.utils.toJobEntity
import kotlinx.coroutines.*

class JobViewModel(
    private val jobsRepository: IJobsRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private val mutableJobState: MutableLiveData<JobState> = MutableLiveData()
    val jobState: LiveData<JobState>
        get() {
            if (mutableJobState.value != null) {
                return mutableJobState
            }
            return mutableJobState.apply {
                mutableJobState.value = JobState.Loading
                fetchJob()
            }
        }

    private fun fetchJob() {
        CoroutineScope(Dispatchers.IO + jobExceptionHandler).launch {
            mutableJobState.postValue(
                when (val fetchedJobs = jobsRepository.getJobs()) {
                    is FetchedJobs.CachedJobs -> {
                        JobState.Offline(
                            fetchedJobs.jobs.getNearest(System.currentTimeMillis() / 1000),
                            fetchedJobs.cacheDate,
                            fetchedJobs.exception
                        )
                    }
                    is FetchedJobs.SuccessJobFetch -> JobState.Online(
                        fetchedJobs.jobs.getNearest(System.currentTimeMillis() / 1000)
                    )
                }
            )
            CoroutineScope(Dispatchers.IO + jobExceptionHandler).launch {
                delay(BuildConfig.REFRESH_RATE.toLong())
                fetchJob()
            }
        }.also { cancelableJobs.add(it) }
    }

    private val jobExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableJobState.postValue(JobState.Error(throwable))
    }

    fun updateJob(jobExpanded: JobExpandedEntity) {
        CoroutineScope(Dispatchers.IO + jobExceptionHandler).launch {
            jobsRepository.updateJobStatus(jobExpanded.toJobEntity())
            mutableJobState.postValue(JobState.Online(jobExpanded))
        }.also { cancelableJobs.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        cancelableJobs.cancel()
    }

    private val cancelableJobs = CancelableJobs()
}