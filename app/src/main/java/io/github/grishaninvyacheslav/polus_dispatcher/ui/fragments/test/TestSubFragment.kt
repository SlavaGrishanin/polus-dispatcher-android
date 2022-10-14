package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentTestSubBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.services.TestService
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.IScreens
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test.SubViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class TestSubFragment : BaseFragment<FragmentTestSubBinding>(FragmentTestSubBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = TestSubFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    private val viewModel: SubViewModel by viewModel()

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.counter.observe(viewLifecycleOwner) { renderCounter(it) }
        binding.increaseCounter.setOnClickListener {
            viewModel.increaseCounter()
        }
        binding.message.text = "fragment: sub $containerTag\ninstance: ${Random.nextInt(0, 100)}"

        Intent(requireContext(), TestService::class.java).also { intent ->
            requireActivity().startForegroundService(intent)
        }
    }

    private fun renderCounter(counter: Int){
        binding.counter.text = "Счётчик: $counter"
    }

    override fun onBackPressed() {
        cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}