package red.code101.app.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
import red.code101.app.databinding.FragmentAuthRegisterBinding
import red.code101.app.ui.auth.AuthConstants
import red.code101.app.ui.auth.register.RegisterViewModel.RegisterEvent
import red.code101.app.utils.*
import red.code101.app.utils.snacks.snackbarAuth
import red.code101.app.utils.window.WindowUtils
import red.code101.app.utils.window.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

private typealias Binding = FragmentAuthRegisterBinding
private typealias Directions = RegisterFragmentDirections

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    // region Fields

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    private var resultIsCancel = true

    // endregion
    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()
            viewModel = this@RegisterFragment.viewModel
            lifecycleOwner = this@RegisterFragment

            // top app bar
            setupTopAppBar()
        }
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
            result = bundleOf(AuthConstants.BundleKeyIsCancel to resultIsCancel)
        )
        navigateBack()
    }

    private fun Binding.setupTopAppBar() {
        onBackPressed { back() }
        topBar.setNavigationOnClickListener { back() }
        (topBar.menu[0].actionView as MaterialButton).apply {
            text = getString(R.string.login)
            setOnClickListener {
                navigateTo(Directions.toLogin())
            }
        }
    }

    private fun observerEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
            is RegisterEvent.SuccessfulAuth -> {
                snackbarAuth(auth = event.auth)
                resultIsCancel = false
                back()
            }
            RegisterEvent.SignUp -> Unit
        }
    }

    // endregion
}