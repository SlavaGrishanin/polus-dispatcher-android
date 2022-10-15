package io.github.grishaninvyacheslav.polus_dispatcher.ui.screens

import com.github.terrakok.cicerone.Screen
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag

interface IScreens {
    fun auth(tabTag: TabTag): Screen
    fun currentJob(tabTag: TabTag): Screen
    fun sheet(tabTag: TabTag): Screen
    fun settings(tabTag: TabTag): Screen


    fun main(tabTag: TabTag): Screen
    fun sub(tabTag: TabTag): Screen
    fun map(tabTag: TabTag): Screen
}