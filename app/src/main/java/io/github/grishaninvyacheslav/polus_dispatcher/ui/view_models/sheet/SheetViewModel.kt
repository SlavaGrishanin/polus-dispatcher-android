package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.IJobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthState
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SheetViewModel(
    private val jobsRepository: IJobsRepository
) : ViewModel() {
    private val mutableSheetState: MutableLiveData<SheetState> = MutableLiveData()
    val sheetState: LiveData<SheetState>
        get() {
            if (mutableSheetState.value == SheetState.Loading) {
                return mutableSheetState
            }
            return mutableSheetState.apply {
                value = SheetState.Loading
                CoroutineScope(Dispatchers.IO + sheetExceptionHandler).launch {
                    postValue(
                        SheetState.Success(
                            jobsRepository.getJobs()
                        )
                    )
                }.also { cancelableJobs.add(it) }
            }
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