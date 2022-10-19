package red.code101.app.utils.window

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import red.code101.app.utils.window.insets.RootViewDeferringInsetsCallback

class WindowUtils {
    companion object {
        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        @JvmStatic
        fun Context.getDimenByName(name: String): Int {
            var result = 0
            val resourceId = resources.getIdentifier(name, "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        @JvmStatic
        fun ViewBinding.fillMarginWhitSetDecorFitsSystemWindows(container: View? = null) {
            (container ?: root).setPadding(
                0,
                root.context.getDimenByName("status_bar_height"),
                0,
                root.context.getDimenByName("navigation_bar_height")
            )
        }

        @JvmStatic
        fun showSoftKeyboardSetup(view: View) {
            val deferringInsetsListener = RootViewDeferringInsetsCallback(
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
            ViewCompat.setOnApplyWindowInsetsListener(view, deferringInsetsListener)
        }
    }
}