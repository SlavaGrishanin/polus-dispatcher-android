package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.DialogStatusPickerBinding
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentCurrentJobBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job.JobState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job.JobViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.utils.timestampToClockTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentJobFragment :
    BaseFragment<FragmentCurrentJobBinding>(FragmentCurrentJobBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = CurrentJobFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(TestSubFragment.CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.jobState.observe(viewLifecycleOwner) { renderJobState(it) }
        with(binding) {
            mapViewBlock.setOnClickListener { }
        }
    }

    private fun renderJobState(state: JobState) = with(binding) {
        Log.d("[MYLOG]", "state: $state")
        when (state) {
            is JobState.Success -> {
                if (state.jobExpanded == null) {
                    dataRoot.isVisible = false
                    placeholder.isVisible = true
                    progressBar.isVisible = false
                    placeholderMessage.text = getString(R.string.no_job)
                    placeholderMessage.isVisible = true
                    statusTitle.isVisible = false
                    status.isVisible = false
                } else {
                    dataRoot.isVisible = true
                    placeholder.isVisible = false
                    statusTitle.isVisible = true
                    status.text = when (state.jobExpanded.status) {

                        "planned" -> getString(R.string.free)
                        "preparing" -> getString(R.string.preparing)
                        "heading" -> getString(R.string.status_heading)
                        "paused" -> getString(R.string.status_paused)
                        "in_progress" -> getString(R.string.status_engaged)
                        "troubled" -> getString(R.string.status_unavailable)
                        "resting" -> getString(R.string.status_resting)
                        "completed" -> getString(R.string.status_completed)
                        else -> getString(R.string.unknown_status)
                    }
                    status.isVisible = true
                    currentJobTitle.text = state.jobExpanded.title

                    binding.mapView.map.mapObjects.addPlacemark(
                        Point(state.jobExpanded.lat.toDouble(), state.jobExpanded.lon.toDouble()),
                        ImageProvider.fromResource(requireContext(), R.drawable.icon_job_location),
                        IconStyle().apply {
                            scale = 0.2f
                        }
                    )

                    binding.mapView.map.move(
                        CameraPosition(
                            Point(state.jobExpanded.lat.toDouble(), state.jobExpanded.lon.toDouble()),
                            11.0f,
                            0.0f,
                            0.0f
                        ),
                        Animation(Animation.Type.SMOOTH, 0F),
                        null
                    )

                    binding.shareWithNavigator.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW);
                        intent.data = Uri.parse("geo:${state.jobExpanded.lat},${state.jobExpanded.lon}?z=11");
                        startActivity(intent)
                    }

                    time.text = String.format(
                        getString(R.string.current_job_time),
                        state.jobExpanded.startDate.timestampToClockTime(),
                        state.jobExpanded.endDate.timestampToClockTime()
                    )
                    requiredVehicle.text =
                        state.jobExpanded.typeVehicle.modelVehicle.model ?: state.jobExpanded.typeVehicle.type
                    state.jobExpanded.customer?.let {
                        customerBackground.isVisible = true
                        customerTitle.isVisible = true
                        customer.isVisible = true
                        customer.text = it.name + "\n" + it.login
                    } ?: run {
                        customerBackground.isVisible = false
                        customerTitle.isVisible = false
                        customer.isVisible = false
                    }
                    status.setOnClickListener {
                        val dialogBuilder: AlertDialog.Builder =
                            AlertDialog.Builder(requireContext())
                        val dialogBinding =
                            DialogStatusPickerBinding.inflate(LayoutInflater.from(context))
                        dialogBuilder.setView(dialogBinding.root)
                        val alertDialog: AlertDialog = dialogBuilder.create()
                        dialogBinding.apply {
                            when (state.jobExpanded.status) {
                                "planned" -> statusFree.isChecked = true
                                "preparing" -> statusPreparing.isChecked = true
                                "heading" -> statusHeading.isChecked = true
                                "paused" -> statusPaused.isChecked = true
                                "in_progress" -> statusEngaged.isChecked = true
                                "troubled" -> statusUnavailable.isChecked = true
                                "resting" -> statusResting.isChecked = true
                                "completed", -> statusCompleted.isChecked = true
                            }
                            statusFree.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "planned"
                                })
                                alertDialog.dismiss()
                            }
                            statusPreparing.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "preparing"
                                })
                                alertDialog.dismiss()
                            }
                            statusHeading.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "heading"
                                })
                                alertDialog.dismiss()
                            }
                            statusPaused.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "paused"
                                })
                                alertDialog.dismiss()
                            }
                            statusEngaged.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "in_progress"
                                })
                                alertDialog.dismiss()
                            }
                            statusUnavailable.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "troubled"
                                })
                                alertDialog.dismiss()
                            }
                            statusResting.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "resting"
                                })
                                alertDialog.dismiss()
                            }
                            statusCompleted.setOnClickListener {
                                viewModel.updateJob(state.jobExpanded.apply {
                                    status = "completed"
                                })
                                alertDialog.dismiss()
                            }
                            cancel.setOnClickListener { alertDialog.dismiss() }
                        }
                        alertDialog.show()
                    }
                }
            }
            JobState.Loading -> {
                dataRoot.isVisible = false
                placeholder.isVisible = true
                progressBar.isVisible = true
                placeholderMessage.text = getString(R.string.loading_job)
                placeholderMessage.isVisible = true
                statusTitle.isVisible = false
                status.isVisible = false
            }
            is JobState.Error -> {
                // TODO()
            }
        }
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
        // TODO: Toast нажмите назад три раза (в течеении времени или по локальному счётчику) чтобы вернуться
        //cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)

    private val viewModel: JobViewModel by viewModel()
}