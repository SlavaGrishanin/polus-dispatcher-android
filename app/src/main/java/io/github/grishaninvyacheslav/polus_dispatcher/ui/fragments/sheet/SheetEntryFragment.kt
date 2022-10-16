package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet

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
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentSheetEntryBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment
import io.github.grishaninvyacheslav.polus_dispatcher.utils.timestampToDate

class SheetEntryFragment :
    BaseFragment<FragmentSheetEntryBinding>(FragmentSheetEntryBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        val JOB_TAG_ARG = "JOB_TAG"
        fun newInstance(containerTag: TabTag, job: JobExpandedEntity) = SheetEntryFragment().apply {
            arguments = Bundle().apply {
                putSerializable(CONTAINER_TAG_ARG, containerTag)
                putSerializable(JOB_TAG_ARG, job)
            }
        }
    }

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    private val job: JobExpandedEntity
        get() = requireArguments().getSerializable(JOB_TAG_ARG) as JobExpandedEntity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            mapBlock.setOnClickListener { }
            title.text = job.title
            time.text = "ВРЕМЯ: ${job.startDate.timestampToDate()}"
            requiredVehicle.text = job.modelVehicle ?: job.typeVehicle
            customer.text = job.customer.name + "\n" + job.customer.login
        }

        binding.mapView.map.mapObjects.addPlacemark(
            Point(job.lat, job.lon),
            ImageProvider.fromResource(requireContext(), R.drawable.icon_job_location),
            IconStyle().apply {
                scale = 0.2f
            }
        )

        binding.mapView.map.move(
            CameraPosition(Point(job.lat, job.lon), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }


    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onBackPressed() {
        cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}