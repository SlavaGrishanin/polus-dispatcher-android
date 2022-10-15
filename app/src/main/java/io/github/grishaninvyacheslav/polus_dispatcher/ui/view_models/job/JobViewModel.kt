package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.IJobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile.IProfileRepository
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import io.github.grishaninvyacheslav.polus_dispatcher.utils.getNearest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobViewModel(
    private val jobsRepository: IJobsRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private val mutableJobState: MutableLiveData<JobState> = MutableLiveData()
    val jobState: LiveData<JobState>
        get() {
            if(mutableJobState.value != null){
                return mutableJobState
            }
            return mutableJobState.apply {
                value = JobState.Loading
                CoroutineScope(Dispatchers.IO + sheetExceptionHandler).launch {
                    postValue(
                        JobState.Success(
                            jobsRepository.getJobs().getNearest(System.currentTimeMillis()/1000)
                        )
                    )
                }.also { cancelableJobs.add(it) }
            }
        }

    private val sheetExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableJobState.postValue(JobState.Error(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        cancelableJobs.cancel()
    }

    private val cancelableJobs = CancelableJobs()
}