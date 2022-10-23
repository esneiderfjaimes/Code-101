package red.code101.app.utils

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import red.code101.app.R

fun Fragment.getDecorView(): View {
    return requireActivity().window.decorView
}

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

fun Fragment.onBackPressed(
    onBackPressed: OnBackPressedCallback.() -> Unit
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        owner = this,
        onBackPressed = onBackPressed
    )
}

fun Toolbar.getMenuItem(id: Int): MenuItem? =
    if (menu.size() > id) menu[id] else null

fun <T> Fragment.connect(liveData: LiveData<T>, observerEvent: (t: T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer(observerEvent))
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
