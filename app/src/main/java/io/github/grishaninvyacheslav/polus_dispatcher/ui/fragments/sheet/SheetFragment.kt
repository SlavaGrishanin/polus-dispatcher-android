package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.sheet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentSheetBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.ISheetDataModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.ISheetItemView
import io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet.SheetListAdapter
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.test.TestSubFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet.SheetState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet.SheetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        initView()
        viewModel.sheetState.observe(viewLifecycleOwner) { renderSheetState(it) }
    }

    private fun initView() = with(binding){

    }

    private fun renderSheetState(state: SheetState) {
        Log.d("[MYLOG]", "renderSheetState: $state")
        with(binding){
            when (state) {
                is SheetState.Success -> {
                    initList(state.sheet)
                    progressBar.isVisible = false
                    sheetList.isVisible = true
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

    private fun initList(log: List<JobEntity>) = with(binding) {
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
        var sheetEntries = listOf<JobEntity>()
        override fun getCount() = sheetEntries.size
        override fun bindView(view: ISheetItemView) = with(sheetEntries[view.pos]) {
            with(view) {
                setTitle(title)
            }
        }
    }
}