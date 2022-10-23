package red.code101.app.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.databinding.FragmentAccountBinding
import red.code101.app.ui.account.AccountViewModel.AccountEvent
import red.code101.app.ui.auth.AuthConstants
import red.code101.app.utils.*
import red.code101.app.utils.window.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

private typealias Binding = FragmentAccountBinding
private typealias Directions = AccountFragmentDirections

@AndroidEntryPoint
class AccountFragment : Fragment() {
    // region Fields

    private val viewModel: AccountViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    // endregion
    // region Override Methods & Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize result to use authentication with google
        viewModel.initAuthWithGoogle(this)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View? {
        if (viewModel.user == null) {
            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResultListener(AuthConstants.RequestKeyAuth) { _, bundle ->
                if (bundle.getBoolean(AuthConstants.BundleKeyIsCancel, false)) {
                    navigateBack()
                }
            }
            navigateTo(Directions.toLogin())
            return null
        }
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows()
            viewModel = this@AccountFragment.viewModel
            lifecycleOwner = this@AccountFragment
            onProviderClick = this@AccountFragment.viewModel::onProviderClick

            // top app bar
            setupTopAppBar()
        }
        connect(viewModel.event, this::observerEvent)
        return binding.root
    }

    // endregion
    // region Private Methods

    private fun Binding.setupTopAppBar() {
        onBackPressed { navigateBack() }
        topBar.setNavigationOnClickListener { navigateBack() }
    }

    private fun observerEvent(event: AccountEvent) {
        when (event) {
            is AccountEvent.ShowError -> toast("Error -> ${event.error.message}")
            AccountEvent.SignOut -> {
                navigateBack()
            }
            is AccountEvent.ShowSnack -> toast(event.msg)
        }
    }

    // endregion
}