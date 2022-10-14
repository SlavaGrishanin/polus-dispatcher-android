package io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag

class LocalCiceroneHolder {
    private val containers = HashMap<TabTag, Cicerone<Router>>()

    fun getCicerone(containerTag: TabTag): Cicerone<Router> =
        containers.getOrPut(containerTag) {
            create()
        }

    fun clear(){
        containers.clear()
    }
}