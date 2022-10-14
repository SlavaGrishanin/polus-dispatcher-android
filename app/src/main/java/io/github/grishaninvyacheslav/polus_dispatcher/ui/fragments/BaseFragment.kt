package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.IScreens
import org.koin.android.ext.android.inject

abstract class BaseFragment<Binding : ViewBinding> protected constructor(
    private val bindingFactory: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> Binding
) : Fragment(), BackButtonListener {
    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = bindingFactory(inflater, container, false).also { _binding = it }.root

    protected val ciceroneHolder: LocalCiceroneHolder by inject()
    protected val screens: IScreens by inject()
}