package red.code101.app.ui.main

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import code101.data.CurrentAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import red.code101.app.utils.GlideUtils
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currentAuth: CurrentAuthUseCase,
) : ViewModel() {

    // region Public Methods

    fun onUserPhoto(context: Context, onResourceReady: (Bitmap) -> Unit) {
        GlideUtils.fromUrl(context, currentAuth.invoke()?.photoUrl.toString(), onResourceReady)
    }

    // endregion

}