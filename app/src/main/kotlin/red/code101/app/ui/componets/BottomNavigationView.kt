package red.code101.app.ui.componets

import android.app.ActionBar
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import red.code101.app.utils.WindowUtils.Companion.getDimenByName

class BottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : BottomNavigationView(context, attrs) {
    private var initHeight: Int = 0

    init {
        val layoutHeight =
            attrs?.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height")
        var height: Int
        when {
            layoutHeight.equals(ViewGroup.LayoutParams.MATCH_PARENT.toString()) ->
                height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutHeight.equals(ViewGroup.LayoutParams.WRAP_CONTENT.toString()) ->
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            else -> context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.layout_height))
                .apply {
                    height = getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT)
                    recycle()
                }
        }

        // Further to do something with `height`:
        when (height) {
            ViewGroup.LayoutParams.MATCH_PARENT -> {
                // defined as `MATCH_PARENT`
            }
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
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
            if (height == ActionBar.LayoutParams.MATCH_PARENT || height == ActionBar.LayoutParams.WRAP_CONTENT) {
                return
            }
            height = initHeight + context.getDimenByName("navigation_bar_height")
        }
        requestLayout()
    }
}