package red.code101.app.ui.componets

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet

fun spToPx(sp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
    ).toInt()
}

class Constraints : ConstraintSet(), ScopeConstraintSet {
    private var currentId: Int = 0

    fun buildIn(view: View, width: Int, height: Int, scope: ScopeConstraintSet.(View) -> Unit) {
        currentId = view.id
        if (currentId == 0) return
        constrainWidth(currentId, width)
        constrainHeight(currentId, height)
        scope.invoke(this, view)
        currentId = 0
    }

    override fun connect(startSide: Int, endID: Int, endSide: Int) {
        if (currentId != 0) connect(currentId, startSide, endID, endSide)
    }

    override fun setMargin(anchor: Int, value: Int) {
        if (currentId != 0) setMargin(currentId, anchor, value)
    }
}

interface ScopeConstraintSet {
    fun connect(startSide: Int, endID: Int, endSide: Int)
    fun setMargin(anchor: Int, value: Int)
}