package io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth

interface IAuthRepository {
    suspend fun signIn(login: String, password: String, rememberMe: Boolean)
    suspend fun getLocalExecutorId(): Int?
    suspend fun signOut()
}