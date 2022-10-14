package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentTestMainBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test.TestMainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class TestMainFragment : BaseFragment<FragmentTestMainBinding>(FragmentTestMainBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = TestMainFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    private val viewModelTest: TestMainViewModel by viewModel()

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(TestSubFragment.CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelTest.counter.observe(viewLifecycleOwner) { renderCounter(it) }
        binding.increaseCounter.setOnClickListener {
            viewModelTest.increaseCounter()
        }
        binding.toSubFragment.setOnClickListener { cicerone.router.navigateTo(screens.sub(containerTag)) }
        binding.toMapFragment.setOnClickListener { cicerone.router.navigateTo(screens.map(containerTag)) }
        binding.message.text = "fragment: main $containerTag\ninstance: ${Random.nextInt(0, 100)}"
    }

    private fun renderCounter(counter: Int){
        binding.counter.text = "Счётчик: $counter"
    }

    override fun onBackPressed() {
        // TODO: Toast нажмите назад три раза (в течеении времени или по локальному счётчику) чтобы вернуться
        //cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}