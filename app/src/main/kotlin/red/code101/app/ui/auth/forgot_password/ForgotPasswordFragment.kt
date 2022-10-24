package red.code101.app.ui.auth.forgot_password

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
import red.code101.app.databinding.FragmentAuthForgotPasswordBinding
import red.code101.app.ui.auth.forgot_password.ForgotPasswordViewModel.ForgotPasswordEvent
import red.code101.app.utils.connect
import red.code101.app.utils.onBackPressed
import red.code101.app.utils.toast
import red.code101.app.utils.window.WindowUtils

private typealias Binding = FragmentAuthForgotPasswordBinding

@AndroidEntryPoint
class ForgotPasswordFragment : BottomSheetDialogFragment() {
    // region Fields

    private val viewModel: ForgotPasswordViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    // endregion
    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        onBackPressed { viewModel.goBack() }
        _binding = Binding.inflate(i, c, false).apply {
            fpViewModel = viewModel
            lifecycleOwner = this@ForgotPasswordFragment
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

    private fun observerEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.GoBack -> dismiss()
            is ForgotPasswordEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
        }
    }

    // endregion
}