package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.FetchedJobs
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.IJobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job.JobState
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import io.github.grishaninvyacheslav.polus_dispatcher.utils.getNearest
import kotlinx.coroutines.*

class SheetViewModel(
    private val jobsRepository: IJobsRepository
) : ViewModel() {
    private val mutableSheetState: MutableLiveData<SheetState> = MutableLiveData()
    val sheetState: LiveData<SheetState>
        get() {
            if (mutableSheetState.value != null) {
                return mutableSheetState
            }
            return mutableSheetState.apply {
                value = SheetState.Loading
                fetchSheet()
            }
        }

    fun fetchSheet() {
        CoroutineScope(Dispatchers.IO + sheetExceptionHandler).launch {
            mutableSheetState.postValue(
                when (val fetchedJobs = jobsRepository.getJobs()) {
                    is FetchedJobs.CachedJobs -> SheetState.Offline(
                        fetchedJobs.jobs,
                        fetchedJobs.cacheDate,
                        fetchedJobs.exception
                    )
                    is FetchedJobs.SuccessJobFetch -> SheetState.Online(
                        fetchedJobs.jobs
                    )
                }
            )
        }.also { cancelableJobs.add(it) }
    }

    private val sheetExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableSheetState.postValue(SheetState.Error(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        cancelableJobs.cancel()
    }

    private val cancelableJobs = CancelableJobs()
}