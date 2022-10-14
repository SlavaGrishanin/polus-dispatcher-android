package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.SignInBody
import io.github.grishaninvyacheslav.polus_dispatcher.model.data_sources.IAuthDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences.IPreferencesRepository
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig
import retrofit2.HttpException
import retrofit2.awaitResponse

class AuthRepository(
    private val authApi: IAuthDataSource,
    private val preferencesRepository: IPreferencesRepository,
) : IAuthRepository {
    private var localExecutorID: String? = null

    private var rememberMe = true

    override suspend fun signIn(login: String, password: String, rememberMe: Boolean) {
        this.rememberMe = rememberMe
        with(authApi.signIn(SignInBody(login, password)).awaitResponse()){
            when(code()){
                200 -> {
                    localExecutorID = body()?.executorId
                    localExecutorID?.let {
                        if (rememberMe) {
                            preferencesRepository.saveString(BuildConfig.PREFERENCES_EXECUTOR_ID_KEY, it)
                        }
                    }
                }
                else -> throw HttpException(this)
            }
        }
    }

    override suspend fun getLocalExecutorId(): String? {
        if (localExecutorID != null) {
            return localExecutorID
        }
        localExecutorID = preferencesRepository.getString(BuildConfig.PREFERENCES_EXECUTOR_ID_KEY)
        return localExecutorID
    }

    override suspend fun signOut() {
        preferencesRepository.removeString(BuildConfig.PREFERENCES_EXECUTOR_ID_KEY)
    }
}