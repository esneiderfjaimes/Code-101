package red.code101.app.ui.componets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.google.android.material.bottomnavigation.BottomNavigationView
import red.code101.app.utils.window.WindowUtils.Companion.getDimenByName

class BottomNavigation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BottomNavigationView(context, attrs) {
    private var initHeight: Int = 0

    init {
        val layoutHeight =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        var height: Int
        when {
            layoutHeight.equals(MATCH_PARENT.toString()) -> height = MATCH_PARENT
            layoutHeight.equals(WRAP_CONTENT.toString()) -> height = WRAP_CONTENT
            else -> context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_height))
                .apply {
                    height = getDimensionPixelSize(0, WRAP_CONTENT)
                    recycle()
                }
        }

        // Further to do something with `height`:
        when (height) {
            MATCH_PARENT -> {
                // defined as `MATCH_PARENT`
            }
            WRAP_CONTENT -> {
                // defined as `WRAP_CONTENT`
            }
            in 0 until Int.MAX_VALUE -> {
                // defined as dimension values (here in pixels)
                initHeight = height
            }
        }
    }

    fun fixDefinedHeight() {
        with(layoutParams) {
            if (height == MATCH_PARENT || height == WRAP_CONTENT) {
                return
            }
            height = initHeight + context.getDimenByName("navigation_bar_height")
        }
        requestLayout()
    }
}