package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth

sealed class AuthState {
    object WaitingAuthData: AuthState()
    object Loading: AuthState()
    object Success: AuthState()
    data class AuthError(val error: Throwable): AuthState()
}