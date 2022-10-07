package red.code101.app.utils.snacks

import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import code101.domain.Auth
import com.google.android.material.snackbar.Snackbar
import red.code101.app.databinding.ViewSnackAuthBinding

fun Fragment.snackbarAuth(view: View, auth: Auth) {

    // create an instance of the snackbar
    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

    // inflate the custom_snackbar_view created previously
    val customSnackView: View =
        ViewSnackAuthBinding.inflate(layoutInflater, null, false).run {
            user = auth
            root
        }

    with(snackbar.view) {
        // set the background of the default snackbar as transparent
        setBackgroundColor(Color.TRANSPARENT)

        setOnClickListener { snackbar.dismiss() }

        // now change the layout of the snackbar
        if (this is Snackbar.SnackbarLayout) {
            // set padding of the all corners as 0
            setPadding(0, 0, 0, 0)

            // add the custom snack bar layout to snackbar layout
            addView(customSnackView, 0)
        }
    }

    snackbar.show()
}