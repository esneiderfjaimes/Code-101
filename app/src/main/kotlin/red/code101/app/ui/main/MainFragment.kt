package red.code101.app.ui.main

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
import red.code101.app.databinding.FragmentMainBinding
import red.code101.app.databinding.NavigationRailFabBinding
import red.code101.app.utils.getMenuItem
import red.code101.app.utils.navigateTo
import red.code101.app.utils.onBackPressed
import red.code101.app.utils.window.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

private typealias Binding = FragmentMainBinding
private typealias Directions = HomeFragmentDirections

@AndroidEntryPoint
class HomeFragment : Fragment() {
    // region Fields

    private val viewModel: MainViewModel by viewModels()

    private var _binding: Binding? = null
    private val binding get() = _binding!!

    // endregion
    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        onBackPressed {
            activity?.moveTaskToBack(true)
        }
        _binding = Binding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows(container)

            // top app bar
            setupTopAppBar()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigation?.fixDefinedHeight()
    }

    // endregion
    // region Private Methods

    private fun Binding.setupTopAppBar() {
        val fabAccount = navRail?.headerView?.let {
            NavigationRailFabBinding.bind(it).fabAccount
        }

        topBar.setOnMenuItemClickListener { menu ->  // Portrait Account
            if (menu.itemId == R.id.page_account) {
                navigateTo(Directions.toAccount())
                true
            } else false
        }
        viewModel.onUserPhoto(requireContext()) { bitmap ->
            topBar.getMenuItem(0)?.icon = BitmapDrawable(resources, bitmap)
            fabAccount?.setImageBitmap(bitmap)
            fabAccount?.imageTintList = null
        }
        fabAccount?.setOnClickListener { navigateTo(Directions.toAccount()) }
    }

    // endregion
}