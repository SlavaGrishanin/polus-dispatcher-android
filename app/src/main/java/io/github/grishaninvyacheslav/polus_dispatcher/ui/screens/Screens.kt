package io.github.grishaninvyacheslav.polus_dispatcher.ui.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.auth.AuthFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job.CurrentJobFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.profile.ProfileFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.settings.SettingsFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet.SheetEntryFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet.SheetFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestMainFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestMapFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment

class Screens: IScreens {
    override fun auth(tabTag: TabTag) = FragmentScreen { AuthFragment.newInstance(tabTag) }
    override fun currentJob(tabTag: TabTag) = FragmentScreen { CurrentJobFragment.newInstance(tabTag) }
    override fun sheet(tabTag: TabTag) = FragmentScreen { SheetFragment.newInstance(tabTag) }
    override fun sheetEntry(tabTag: TabTag, job: JobExpandedEntity) = FragmentScreen { SheetEntryFragment.newInstance(tabTag, job) }
    override fun profile(tabTag: TabTag) = FragmentScreen { ProfileFragment.newInstance(tabTag) }
    override fun settings(tabTag: TabTag) = FragmentScreen { SettingsFragment.newInstance(tabTag) }


    override fun main(tabTag: TabTag) = FragmentScreen { TestMainFragment.newInstance(tabTag) }
    override fun sub(tabTag: TabTag) = FragmentScreen { TestSubFragment.newInstance(tabTag) }
    override fun map(tabTag: TabTag) = FragmentScreen { TestMapFragment.newInstance(tabTag) }
}