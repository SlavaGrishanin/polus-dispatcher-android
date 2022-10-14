package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity

sealed class SheetState {
    object Loading: SheetState()
    data class Error(val error: Throwable): SheetState()
    data class Success(val sheet: List<JobEntity>): SheetState()
}