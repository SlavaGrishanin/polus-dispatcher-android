package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.ResponseStatus
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface IJobsDataSource {
    @GET("executor/jobs/{executorId}")
    fun executorJobs(@Path("executorId") executorId: String): Call<List<JobExpandedEntity>>

    @PUT("customer/job")
    fun updateJob(@Body jobEntity: JobEntity): Call<ResponseStatus>
}