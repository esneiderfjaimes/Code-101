package red.code101.app.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import red.code101.app.R

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(@StringRes resId: Int) {
    toast(requireContext().getString(resId))
}

fun Fragment.navigateTo(directions: NavDirections) {
    lifecycleScope.launchWhenStarted {
        try {
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(directions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Fragment.navigateBack() {
    lifecycleScope.launchWhenStarted {
        Navigation.findNavController(requireActivity(), R.id.fragment_container)
            .popBackStack()
    }
}

fun Context.animIn(view: View, @AnimRes id: Int) {
    view.startAnimation(AnimationUtils.loadAnimation(this, id))
}
