package io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig
import io.github.grishaninvyacheslav.polus_dispatcher.R
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.FragmentAuthBinding
import io.github.grishaninvyacheslav.polus_dispatcher.ui.IBottomNavigation
import io.github.grishaninvyacheslav.polus_dispatcher.ui.TabTag
import io.github.grishaninvyacheslav.polus_dispatcher.ui.fragments.BaseFragment
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthState
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {
    companion object {
        val CONTAINER_TAG_ARG = "CONTAINER_TAG"
        fun newInstance(containerTag: TabTag) = AuthFragment().apply {
            arguments = Bundle().apply { putSerializable(CONTAINER_TAG_ARG, containerTag) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authState.observe(viewLifecycleOwner) { renderAuthState(it) }
        signIn.buttonClickListener = {
            viewModel.signIn(
                loginEditText.text.toString(),
                passwordEditText.text.toString(),
                rememberMe.isChecked
            )
        }

        // https://stackoverflow.com/a/29449717
        with(loginEditText){
            post {
                addTextChangedListener {
                    viewModel.cancelAction()
                }
            }
        }
        with(passwordEditText){
            post{
                addTextChangedListener {
                    viewModel.cancelAction()
                }
            }
        }
    }

    private fun renderAuthState(authState: AuthState) =
        when (authState) {
            AuthState.WaitingAuthData -> with(binding) {
                signIn.isLoading = false
                login.error = null
                password.error = null
            }
            AuthState.Loading -> with(binding) {
                signIn.isLoading = true
            }
            AuthState.Success -> with(binding) {
                signIn.isLoading = false
                login.error = null
                password.error = null
                (requireActivity() as IBottomNavigation).openTabWithNavigationReset(TabTag.CURRENT_JOB)
            }
            is AuthState.AuthError -> {
                binding.signIn.isLoading = false
                handleAuthError(authState.error)
            }
        }

    private fun handleAuthError(error: Throwable) {
        when (error) {
            is retrofit2.HttpException -> handleHttpException(error)
            is java.net.ConnectException ->
                Toast.makeText(
                    requireContext(),
                    getString(R.string.no_internet_connection_error),
                    Toast.LENGTH_LONG
                ).show()
            is java.net.SocketTimeoutException ->
                Toast.makeText(
                    requireContext(),
                    String.format(
                        getString(R.string.server_not_responding_error),
                        BuildConfig.API_URL
                    ),
                    Toast.LENGTH_LONG
                ).show()
            else ->
                Toast.makeText(
                    requireContext(),
                    String.format(getString(R.string.unknown_error), error),
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    private fun handleHttpException(exception: retrofit2.HttpException) {
        when (exception.code()) {
            403 -> with(binding) {
                login.error = getString(R.string.wrong_login_or_password_error)
                password.error = getString(R.string.wrong_login_or_password_error)
            }
            else ->
                Toast.makeText(
                    requireContext(),
                    String.format(getString(R.string.server_error), exception),
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    override fun onBackPressed() {
        cicerone.router.exit()
    }

    private val viewModel: AuthViewModel by viewModel()

    private val containerTag: TabTag
        get() = requireArguments().getSerializable(CONTAINER_TAG_ARG) as TabTag

    private val cicerone: Cicerone<Router>
        get() = ciceroneHolder.getCicerone(containerTag)
}