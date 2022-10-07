package red.code101.app.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.viewbinding.ViewBinding

class WindowUtils {
    companion object {
        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        @JvmStatic
        private fun Context.getDimenByName(name: String): Int {
            var result = 0
            val resourceId = resources.getIdentifier(name, "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        @JvmStatic
        fun ViewBinding.fillMarginWhitSetDecorFitsSystemWindows() {
            root.setPadding(
                0,
                root.context.getDimenByName("status_bar_height"),
                0,
                root.context.getDimenByName("navigation_bar_height")
            )
        }
    }
}