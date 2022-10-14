package io.github.grishaninvyacheslav.polus_dispatcher.model.data_sources

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.SignInBody
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface IAuthDataSource {
    @POST("signIn")
    fun signIn(@Body signInBody: SignInBody): Call<SignInResponse>
}