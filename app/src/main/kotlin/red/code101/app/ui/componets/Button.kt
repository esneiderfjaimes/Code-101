package red.code101.app.ui.componets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.view.setPadding
import com.google.android.material.R
import com.google.android.material.button.MaterialButton

class Button @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.materialButtonStyle
) : ConstraintLayout(context, attrs) {

    private val progressBar: ProgressBar = ProgressBar(context).apply {
        id = generateViewId()
        setPadding(spToPx(4f, context))
        isIndeterminate = true
        this@Button.addView(this)
    }

    private val button: MaterialButton = MaterialButton(context, attrs, defStyleAttr).apply {
        id = View.generateViewId()
        this@Button.addView(this)
    }

    init {
        // Container
        setPadding(0)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        // Constrains
        val constraints = Constraints()
        constraints.buildIn(
            view = button,
            width = MATCH_CONSTRAINT,
            height = MATCH_CONSTRAINT
        ) {
            connect(START, PARENT_ID, START)
            connect(TOP, PARENT_ID, TOP)
            connect(END, PARENT_ID, END)
            connect(BOTTOM, PARENT_ID, BOTTOM)
        }
        constraints.buildIn(
            view = progressBar,
            width = spToPx(40f, context),
            height = spToPx(40f, context)
        ) {
            setMargin(END, spToPx(8f, context))
            connect(TOP, button.id, TOP)
            connect(END, button.id, END)
            connect(BOTTOM, button.id, BOTTOM)
        }

        constraints.applyTo(this)
        button.textColors.defaultColor.apply {
        }
    }

    var isLoading = false
        set(value) {
            progressBar.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }
}
