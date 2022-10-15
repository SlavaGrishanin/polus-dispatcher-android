package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
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
            status.setOnClickListener {
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val dialogBinding = DialogStatusPickerBinding.inflate(LayoutInflater.from(context))
                dialogBuilder.setView(dialogBinding.root)
                val alertDialog: AlertDialog = dialogBuilder.create()
                dialogBinding.apply {
                    statusFree.setOnClickListener { }
                    statusPreparing.setOnClickListener { }
                    statusHeading.setOnClickListener { }
                    statusPaused.setOnClickListener { }
                    statusEngaged.setOnClickListener { }
                    statusUnavailable.setOnClickListener { }
                    statusResting.setOnClickListener { }
                    cancel.setOnClickListener { alertDialog.dismiss() }
                }
                alertDialog.show()
            }
        }
    }

    private fun renderJobState(state: JobState) = with(binding) {
        Log.d("[MYLOG]", "state: $state")
        when (state) {
            is JobState.Success -> {
                if (state.job == null) {
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
                    status.isVisible = true
                    currentJobTitle.text = state.job.title
                    time.text = String.format(
                        getString(R.string.current_job_time),
                        state.job.startDate.timestampToClockTime(),
                        state.job.endDate.timestampToClockTime()
                    )
                    requiredVehicle.text =
                        state.job.requiredVehicle.model ?: state.job.requiredVehicle.characteristic
                    customer.text = state.job.customerId

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

    override fun onBackPressed() {
        // TODO: Toast нажмите назад три раза (в течеении времени или по локальному счётчику) чтобы вернуться
        //cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)

    private val viewModel: JobViewModel by viewModel()
}