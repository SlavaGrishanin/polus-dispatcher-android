package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

sealed class JobState {
    object Loading: JobState()
    data class Success(val job: JobEntity?): JobState()
    data class Error(val error: Throwable): JobState()
}