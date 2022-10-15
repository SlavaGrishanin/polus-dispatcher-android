package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentTabContainerBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.IBottomNavigation
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BackButtonListener
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment

class CurrentJobTabContainerFragment :
    BaseFragment<FragmentTabContainerBinding>(FragmentTabContainerBinding::inflate) {
    companion object {
        private const val CONTAINER_TAG_ARG = "CONTAINER_TAG"

        fun newInstance(containerTag: TabTag) =
            CurrentJobTabContainerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CONTAINER_TAG_ARG, containerTag)
                }
            }
    }

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as IBottomNavigation).isBottomNavigationVisible = true
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(screens.currentJob(containerTag))
        }
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
        val fragment = childFragmentManager.findFragmentById(R.id.ftc_container)
        if (fragment != null && fragment is BackButtonListener) {
            (fragment as BackButtonListener).onBackPressed()
        }
    }

    private val navigator: Navigator by lazy {
        AppNavigator(requireActivity(), R.id.ftc_container, childFragmentManager)
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}