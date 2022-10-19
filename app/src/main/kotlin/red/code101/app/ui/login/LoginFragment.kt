package red.code101.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.databinding.FragmentLoginBinding
import red.code101.app.ui.login.LoginViewModel.LoginEvent
import red.code101.app.utils.snacks.snackbarAuth
import red.code101.app.utils.toast
import red.code101.app.utils.window.WindowUtils
import red.code101.app.utils.window.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

@AndroidEntryPoint
class LoginFragment : Fragment() {

    // region Fields

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // endregion

    // region Override Methods & Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize result to use authentication with google
        viewModel.initAuthWithGoogle(this)

        viewModel.getAuth()?.let {
            observerEvent(LoginEvent.SuccessfulAuth(it))
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? =
        if (true || viewModel.needInflate) {
            _binding = FragmentLoginBinding.inflate(i, c, false).apply {
                viewModel = this@LoginFragment.viewModel
                lifecycleOwner = this@LoginFragment

                passwordInput.doOnTextChanged { text, _, _, _ ->
                    binding.forgotBtn.visibility =
                        if (text.isNullOrBlank()) View.VISIBLE else View.GONE
                }

                fillMarginWhitSetDecorFitsSystemWindows()
            }
            viewModel.event.observe(viewLifecycleOwner, Observer(this::observerEvent))
            binding.root
        } else null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WindowUtils.showSoftKeyboardSetup(binding.root)
    }

    // endregion

    // region Private Methods

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