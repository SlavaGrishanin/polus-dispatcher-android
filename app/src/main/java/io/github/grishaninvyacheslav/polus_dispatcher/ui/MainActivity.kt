package io.github.grishaninvyacheslav.polus_dispatcher.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yandex.mapkit.MapKitFactory
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.ActivityMainBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BackButtonListener
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.auth.AuthTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job.CurrentJobTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.profile.ProfileTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.settings.SettingsTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet.SheetTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestTabContainerFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.main.CurrentTabState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.main.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), IBottomNavigation {
    companion object {
        val BOTTOM_NAV_VISIBILITY_ARG = "BOTTOM_NAV_VISIBILITY"
    }

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //initResizeOnSoftInput()
        setContentView(binding.root)
        MapKitFactory.initialize(applicationContext)
        initViews()
        if (savedInstanceState == null) {
            viewModel.currentTabState.observe(this) { renderTabState(it) }
            viewModel.onFirstViewAttach()
        } else {
            isBottomNavigationVisible = savedInstanceState.getBoolean(BOTTOM_NAV_VISIBILITY_ARG)
        }
        binding.root.setOnClickListener {
            hideKeyboard()
        }
    }

    private fun initViews() = with(binding) {
        bottomNavigationBar.isVisible = isBottomNavigationVisible
        bottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.currentJob -> openTab(TabTag.CURRENT_JOB)
                R.id.sheet -> openTab(TabTag.SHEET)
                R.id.profile -> openTab(TabTag.PROFILE)
                R.id.settings -> openTab(TabTag.SETTINGS)
            }
            return@setOnItemSelectedListener true
        }
    }

    override var isBottomNavigationVisible: Boolean = false
        set(value) {
            field = value
            binding.bottomNavigationBar.isVisible = value
        }

    private fun openTab(tabTag: TabTag) {
        val fm = supportFragmentManager
        var currentFragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                currentFragment = f
                break
            }
        }
        val newFragment = fm.findFragmentByTag(tabTag.name)
        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return
        val transaction = fm.beginTransaction()
        if (newFragment == null) {
            val tabContainerFragment = when (tabTag) {
                TabTag.AUTHORIZATION -> AuthTabContainerFragment.newInstance(tabTag)
                TabTag.CURRENT_JOB -> CurrentJobTabContainerFragment.newInstance(tabTag)
                TabTag.SHEET -> SheetTabContainerFragment.newInstance(tabTag)
                TabTag.PROFILE -> ProfileTabContainerFragment.newInstance(tabTag)
                TabTag.SETTINGS -> SettingsTabContainerFragment.newInstance(tabTag)
            }
            transaction.add(
                R.id.container,
                tabContainerFragment, tabTag.name
            )
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment)
        }
        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    override fun openTabWithNavigationReset(tabTag: TabTag) {
        ciceroneHolder.clear()
        val transaction = supportFragmentManager.beginTransaction()
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null) {
                transaction.remove(fragment)
            }
        }
        transaction.commitNow()
        with(binding) {
            when (tabTag) {
                TabTag.AUTHORIZATION -> openTab(tabTag)
                TabTag.CURRENT_JOB -> bottomNavigationBar.selectedItemId = R.id.currentJob
                TabTag.SHEET -> bottomNavigationBar.selectedItemId = R.id.sheet
                TabTag.PROFILE -> bottomNavigationBar.selectedItemId = R.id.profile
                TabTag.SETTINGS -> bottomNavigationBar.selectedItemId = R.id.settings
            }
        }
    }

    override fun showErrorMessage(message: String) {
        binding.errorMessage.text = message
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        var fragment: Fragment? = null
        val fragments = fm.fragments
        for (f in fragments) {
            if (f.isVisible) {
                fragment = f
                break
            }
        }
        if (fragment != null && fragment is BackButtonListener) {
            (fragment as BackButtonListener).onBackPressed()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(BOTTOM_NAV_VISIBILITY_ARG, isBottomNavigationVisible)
    }

    private val ciceroneHolder: LocalCiceroneHolder by inject()

    // https://stackoverflow.com/a/17789187
    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // https://stackoverflow.com/a/70541781
    private fun initResizeOnSoftInput() {
        var shouldResize = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(shouldResize)
            shouldResize = shouldResize.not()

            binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                    binding.root.setPadding(0, 0, 0, imeHeight)
                }

                windowInsets
            }
        } else {
            @Suppress("DEPRECATION")
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun renderTabState(currentTabState: CurrentTabState) {
        when (currentTabState) {
            is CurrentTabState.TabSelected -> openTab(currentTabState.selectedTabTag)
            is CurrentTabState.InitError -> Toast.makeText(applicationContext, String.format(getString(R.string.unknown_error, currentTabState.error)), Toast.LENGTH_LONG).show()
        }
    }
}