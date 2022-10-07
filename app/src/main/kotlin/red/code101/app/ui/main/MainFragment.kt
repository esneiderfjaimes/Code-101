package red.code101.app.ui.main

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import red.code101.app.R
import red.code101.app.databinding.FragmentMainBinding
import red.code101.app.databinding.NavigationRailFabBinding
import red.code101.app.utils.WindowUtils.Companion.fillMarginWhitSetDecorFitsSystemWindows

@AndroidEntryPoint
class HomeFragment : Fragment() {

    // region Fields

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    // endregion

    // region Override Methods & Callbacks

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.moveTaskToBack(true)
        }
        _binding = FragmentMainBinding.inflate(i, c, false).apply {
            fillMarginWhitSetDecorFitsSystemWindows(container)
            viewModel.onUserPhoto(requireContext()) { bitmap ->
                if (topBar.menu.size() > 0) {
                    topBar.menu[0].icon = BitmapDrawable(resources, bitmap)
                }
                navRail?.headerView?.let {
                    NavigationRailFabBinding.bind(it).fabAccount.apply {
                        setImageBitmap(bitmap)
                        imageTintList = null
                    }
                }
            }
            navRail?.headerView?.let {
                NavigationRailFabBinding.bind(it).fabAccount.setOnClickListener {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                    findNavController().navigate(R.id.loginFragment, null, navOptions)
                }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigation?.fixDefinedHeight()
    }

    // endregion

}