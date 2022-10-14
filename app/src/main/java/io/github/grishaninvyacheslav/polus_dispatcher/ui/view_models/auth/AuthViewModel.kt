package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository
import io.github.grishaninvyacheslav.polus_dispatcher.utils.CancelableJobs
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: IAuthRepository
) : ViewModel() {
    private val mutableAuthState: MutableLiveData<AuthState> = MutableLiveData()
    val authState: LiveData<AuthState> = mutableAuthState

    fun signIn(login: String, password: String, rememberMe: Boolean) {
        if (authState.value == AuthState.Loading) {
            return
        }
        mutableAuthState.value = AuthState.Loading
        CoroutineScope(Dispatchers.IO + authExceptionHandler).launch {
            authRepository.signIn(login, password, rememberMe)
            mutableAuthState.postValue(AuthState.Success)
        }.also { cancelableJobs.add(it) }
    }

    fun cancelAction() {
        cancelableJobs.cancel()
        mutableAuthState.value = AuthState.WaitingAuthData
    }

    private val authExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableAuthState.postValue(AuthState.AuthError(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        cancelableJobs.cancel()
    }

    private val cancelableJobs = CancelableJobs()
}