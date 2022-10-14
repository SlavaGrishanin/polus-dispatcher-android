package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.main

import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag

sealed class CurrentTabState {
    data class TabSelected(val selectedTabTag: TabTag): CurrentTabState()
    data class InitError(val error: Throwable): CurrentTabState()
}