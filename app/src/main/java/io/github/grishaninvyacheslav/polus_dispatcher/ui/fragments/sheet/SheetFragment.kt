package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.yandex.mapkit.MapKitFactory
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentSheetBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobExpandedEntity
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.ISheetDataModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.ISheetItemView
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.SheetListAdapter
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet.SheetState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet.SheetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.yandex.runtime.image.ImageProvider
import io.github.grishaninvyacheslav.polus_dispatcher.ui.IBottomNavigation
import io.github.grishaninvyacheslav.polus_dispatcher.utils.timestampToDate
import java.net.ConnectException

class SheetFragment : BaseFragment<FragmentSheetBinding>(FragmentSheetBinding::inflate) {

    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = SheetFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(TestSubFragment.CONTAINER_TAG_ARG) as TabTag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refresh.setOnClickListener { viewModel.fetchSheet() }
        viewModel.sheetState.observe(viewLifecycleOwner) { renderSheetState(it) }
    }

    private fun renderSheetState(state: SheetState) {
        Log.d("[MYLOG]", "renderSheetState: $state")
        with(binding) {
            when (state) {
                is SheetState.Online -> {
                    showSheetData(state.sheet)
                    (requireActivity() as IBottomNavigation).showErrorMessage("")
                }
                is SheetState.Offline -> {
                    state.sheet?.let { showSheetData(it) }
                    when (state.exception) {
                        is ConnectException -> String.format(
                            getString(R.string.internet_is_lost_used_cache),
                            state.offlineDate.timestampToDate()
                        )
                        is java.net.SocketTimeoutException -> String.format(
                            getString(R.string.server_is_lost_used_cache),
                            state.offlineDate.timestampToDate()
                        )
                        else -> String.format(
                            getString(R.string.unknown_error),
                            state.exception.message
                        )
                    }
                }
                SheetState.Loading -> {
                    progressBar.isVisible = true
                    sheetList.isVisible = false
                }
                is SheetState.Error -> {
                    // TODO()
                }
            }
        }
    }

    private fun showSheetData(sheet: List<JobExpandedEntity>) = with(binding) {
        initList(sheet)
        progressBar.isVisible = false
        sheetList.isVisible = true
    }

    private fun initList(log: List<JobExpandedEntity>) = with(binding) {
        sheetList.layoutManager = LinearLayoutManager(requireContext())
        adapter = SheetListAdapter(
            sheetDataModel.apply { sheetEntries = log },
            onItemClick = { cicerone.router.navigateTo(screens.sheetEntry(containerTag)) },
        )
        sheetList.adapter = adapter
    }

    override fun onBackPressed() {
        // TODO: Toast нажмите назад три раза (в течеении времени или по локальному счётчику) чтобы вернуться
        //cicerone.router.exit()
    }

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)

    private val viewModel: SheetViewModel by viewModel()

    private var adapter: SheetListAdapter? = null

    private val sheetDataModel = object : ISheetDataModel {
        var sheetEntries = listOf<JobExpandedEntity>()
        override fun getCount() = sheetEntries.size
        override fun bindView(view: ISheetItemView) = with(sheetEntries[view.pos]) {
            with(view) {
                setTitle(title)
                setLocation(
                    lat.toDouble(),
                    lon.toDouble(),
                    ImageProvider.fromResource(requireContext(), R.drawable.icon_job_location)
                )
                setStartDate(startDate)
                setVehiclesDescription(modelVehicle ?: typeVehicle)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}