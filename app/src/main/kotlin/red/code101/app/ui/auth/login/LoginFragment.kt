package red.code101.app.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
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
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()
            viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment

            // top app bar
            setupTopAppBar()

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

    private fun back() {
        setFragmentResult(
            requestKey = AuthConstants.RequestKeyAuth,
            result = bundleOf(AuthConstants.BundleKeyIsCancel to true)
        )
        navigateBack()
    }

    private fun Binding.setupTopAppBar() {
        onBackPressed { back() }
        topBar.setNavigationOnClickListener { back() }
        (topBar.menu[0].actionView as MaterialButton).apply {
            text = getString(R.string.register)
            setOnClickListener {
                navigateTo(Directions.toRegister())
            }
        }
    }

    private fun observerEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
            is LoginEvent.SuccessfulAuth -> {
                snackbarAuth(auth = event.auth)
            }
            LoginEvent.SignUp -> Unit
        }
    }

    // endregion
}