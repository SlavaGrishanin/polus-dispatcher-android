package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import retrofit2.HttpException

sealed class JobState {
    object Loading: JobState()
    data class Online(val job: JobExpandedEntity?): JobState()
    data class Offline(val job: JobExpandedEntity?, val offlineDate: Long, val exception: Exception): JobState()
    data class Error(val error: Throwable): JobState()
}