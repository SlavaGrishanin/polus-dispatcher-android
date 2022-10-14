package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.auth

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentTabContainerBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.IBottomNavigation
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseTabContainerFragment

class AuthTabContainerFragment : BaseTabContainerFragment<FragmentTabContainerBinding>(
    FragmentTabContainerBinding::inflate,
    R.id.ftc_container
) {
    companion object {
        private const val CONTAINER_TAG_ARG = "CONTAINER_TAG"

        fun newInstance(containerTag: TabTag) =
            AuthTabContainerFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(CONTAINER_TAG_ARG, containerTag)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as IBottomNavigation).isBottomNavigationVisible = false
        if (childFragmentManager.findFragmentById(R.id.ftc_container) == null) {
            cicerone.router.replaceScreen(screens.auth(containerTag))
        }
    }

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    override val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}