package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity

sealed class JobState {
    object Loading: JobState()
    data class Success(val jobExpanded: JobExpandedEntity?): JobState()
    data class Error(val error: Throwable): JobState()
}