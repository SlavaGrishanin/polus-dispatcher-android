package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test

import android.os.Bundle
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentTestMapBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.IScreens
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test.SubViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random


class TestMapFragment : BaseFragment<FragmentTestMapBinding>(FragmentTestMapBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = TestMapFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    private val viewModel: SubViewModel by viewModel()

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.map.mapObjects.addPlacemark(
            Point(59.865000, 92.915556),
            ImageProvider.fromResource(requireContext(), R.drawable.icon_job_location),
            IconStyle().apply {
                scale = 0.2f
            }
        )

        binding.mapView.map.move(
            CameraPosition(Point(59.865000, 92.915556), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )

        binding.message.text = "fragment: sub $containerTag\ninstance: ${Random.nextInt(0, 100)}"
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onBackPressed() {
        cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}