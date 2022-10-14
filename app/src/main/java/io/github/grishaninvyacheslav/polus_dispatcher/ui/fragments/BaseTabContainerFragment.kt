package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.IScreens
import org.koin.android.ext.android.inject

abstract class BaseTabContainerFragment<Binding : ViewBinding> protected constructor(
    bindingFactory: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> Binding,
    @IdRes private val ftcContainerId: Int
) : BaseFragment<Binding>(bindingFactory) {

    abstract val cicerone: Cicerone<Router>

    protected val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), ftcContainerId, childFragmentManager)
    }

    override fun onResume() {
        super.onResume()
        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        cicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = childFragmentManager.findFragmentById(ftcContainerId)
        if (fragment != null && fragment is BackButtonListener) {
            (fragment as BackButtonListener).onBackPressed()
        }
    }
}