package red.code101.app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.databinding.FragmentLoginBinding
import red.code101.app.ui.login.LoginViewModel.LoginEvent
import red.code101.app.utils.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows
import red.code101.app.utils.snacks.snackbarAuth

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
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()

            signInGoogleBtn.setOnClickListener {
                viewModel.launchSignInGoogle()
            }
        }
        viewModel.event.observe(viewLifecycleOwner, Observer(this::observerEvent))
        return binding.root
    }

    // endregion

    // region Private Methods

    private fun observerEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ShowError -> Unit
            is LoginEvent.SuccessfulAuth -> {
                snackbarAuth(binding.root, event.auth)
            }
            LoginEvent.SignUp -> Unit
        }
    }

    // endregion

}