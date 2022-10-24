package red.code101.app.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.databinding.FragmentAuthLoginBinding
import red.code101.app.ui.auth.AuthConstants
import red.code101.app.ui.auth.login.LoginViewModel.LoginEvent
import red.code101.app.utils.*
import red.code101.app.utils.snacks.snackbarAuth
import red.code101.app.utils.window.WindowUtils
import red.code101.app.utils.window.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

private typealias Binding = FragmentAuthLoginBinding
private typealias Directions = LoginFragmentDirections

@AndroidEntryPoint
class LoginFragment : Fragment() {
    // region Fields

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    // endregion
    // region Override Methods & Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize result to use authentication with google
        viewModel.initAuthWithGoogle(this)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        onBackPressed { viewModel.goBack() }
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()
            loginViewModel = viewModel
            lifecycleOwner = this@LoginFragment

            passwordInput.doOnTextChanged { text, _, _, _ ->
                binding.forgotBtn.visibility =
                    if (text.isNullOrBlank()) View.VISIBLE else View.GONE
            }
        }
        // livedata
        connect(viewModel.event, this::observerEvent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WindowUtils.showSoftKeyboardSetup(binding.root)
    }

    // endregion
    // region Private Methods

    private fun observerEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.GoBack -> {
                setFragmentResult(
                    requestKey = AuthConstants.RequestKeyAuth,
                    result = bundleOf(AuthConstants.BundleKeyIsCancel to event.resultIsCancel)
                )
                navigateBack()
            }
            LoginEvent.GoRegister -> navigateTo(Directions.toRegister())
            LoginEvent.ShowForgotPassword -> navigateTo(Directions.showForgotPassword())
            is LoginEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
            is LoginEvent.SuccessfulAuth -> {
                snackbarAuth(auth = event.auth)
                viewModel.goBack(false)
            }
        }
    }

    // endregion
}