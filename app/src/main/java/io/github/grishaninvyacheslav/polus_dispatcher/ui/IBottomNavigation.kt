package io.github.grishaninvyacheslav.polus_dispatcher.ui

interface IBottomNavigation {
    var isBottomNavigationVisible: Boolean
    fun openTabWithNavigationReset(tabTag: TabTag)
}