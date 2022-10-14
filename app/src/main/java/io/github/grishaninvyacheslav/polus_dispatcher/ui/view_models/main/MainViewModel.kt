package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val authRepository: IAuthRepository) : ViewModel() {
    private val mutableCurrentTabState: MutableLiveData<CurrentTabState> = MutableLiveData()
    val currentTabState: LiveData<CurrentTabState> = mutableCurrentTabState

    fun onFirstViewAttach() {
        CoroutineScope(Dispatchers.IO + initExceptionHandler).launch {
            if (authRepository.getLocalExecutorId() == null) {
                mutableCurrentTabState.postValue(CurrentTabState.TabSelected(TabTag.AUTHORIZATION))
            } else {
                mutableCurrentTabState.postValue(CurrentTabState.TabSelected(TabTag.PAGE_1))
            }
        }.also { cancelableJobs.add(it) }
    }

    private val initExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableCurrentTabState.value = CurrentTabState.InitError(throwable)
    }

    override fun onCleared() {
        super.onCleared()
        cancelableJobs.cancel()
    }

    private val cancelableJobs = CancelableJobs()
}