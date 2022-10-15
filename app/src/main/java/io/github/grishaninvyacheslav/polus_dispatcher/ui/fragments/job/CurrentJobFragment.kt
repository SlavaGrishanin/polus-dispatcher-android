package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.job

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.DialogStatusPickerBinding
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentCurrentJobBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment


class CurrentJobFragment : BaseFragment<FragmentCurrentJobBinding>(FragmentCurrentJobBinding::inflate) {

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
        with(binding){
            status.setOnClickListener {
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val dialogBinding = DialogStatusPickerBinding.inflate(LayoutInflater.from(context))
                dialogBuilder.setView(dialogBinding.root)
                val alertDialog: AlertDialog = dialogBuilder.create()
                dialogBinding.apply {
                    statusFree.setOnClickListener {  }
                    statusPreparing.setOnClickListener {  }
                    statusHeading.setOnClickListener {  }
                    statusPaused.setOnClickListener {  }
                    statusEngaged.setOnClickListener {  }
                    statusUnavailable.setOnClickListener {  }
                    statusResting.setOnClickListener {  }
                    cancel.setOnClickListener { alertDialog.dismiss() }
                }
                alertDialog.show()
            }
        }
    }

    override fun onBackPressed() {
        // TODO: Toast нажмите назад три раза (в течеении времени или по локальному счётчику) чтобы вернуться
        //cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}