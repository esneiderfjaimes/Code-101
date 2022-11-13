package red.code101.app.ui.auth.link_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.databinding.FragmentAuthLinkPasswordBinding
import red.code101.app.ui.auth.AuthConstants
import red.code101.app.ui.auth.link_password.LinkPasswordViewModel.LinkPasswordEvent
import red.code101.app.utils.connect
import red.code101.app.utils.onBackPressed
import red.code101.app.utils.toast
import red.code101.app.utils.window.WindowUtils

private typealias Binding = FragmentAuthLinkPasswordBinding

@AndroidEntryPoint
class LinkPasswordFragment : BottomSheetDialogFragment() {
    // region Fields

    private val viewModel: LinkPasswordViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    // endregion
    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        onBackPressed { viewModel.goBack() }
        _binding = Binding.inflate(i, c, false).apply {
            lpViewModel = viewModel
            lifecycleOwner = this@LinkPasswordFragment
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

    private fun observerEvent(event: LinkPasswordEvent) {
        when (event) {
            LinkPasswordEvent.GoBack -> {
                dismiss()
                setFragmentResult(
                    requestKey = AuthConstants.RequestKeyRequiresReload,
                    result = bundleOf()
                )
            }
            is LinkPasswordEvent.ShowError -> {
                event.error.message?.let { toast(it) }
            }
        }
    }

    // endregion
}