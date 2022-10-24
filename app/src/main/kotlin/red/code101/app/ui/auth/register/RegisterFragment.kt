package red.code101.app.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
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

    // endregion
    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        onBackPressed { viewModel.goBack() }
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()
            registerViewModel = viewModel
            lifecycleOwner = this@RegisterFragment
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

    private fun observerEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.GoBack -> {
                setFragmentResult(
                    requestKey = AuthConstants.RequestKeyAuth,
                    result = bundleOf(AuthConstants.BundleKeyIsCancel to event.resultIsCancel)
                )
                navigateBack()
            }
            RegisterEvent.GoLogin -> {
                navigateTo(Directions.toLogin())
            }
            is RegisterEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
            is RegisterEvent.SuccessfulAuth -> {
                snackbarAuth(auth = event.auth)
                viewModel.goBack(false)
            }
        }
    }

    // endregion
}