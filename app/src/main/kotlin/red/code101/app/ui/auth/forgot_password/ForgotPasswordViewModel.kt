package red.code101.app.ui.auth.forgot_password

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.auth.SendPasswordResetEmailUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendPasswordResetEmail: SendPasswordResetEmailUserCase
) : ViewModel() {
    // region Fields

    private val _event = MutableLiveData<ForgotPasswordEvent>()
    val event: LiveData<ForgotPasswordEvent> get() = _event

    // endregion
    // region Public Methods

    fun goBack() {
        _event.postValue(ForgotPasswordEvent.GoBack)
    }

    fun sendPasswordResetEmail(email: Editable?) {
        viewModelScope.launch {
            sendPasswordResetEmail.invoke(
                email = email.toString().trim(),
            ).catch { exception ->
                Log.e(tag, "sendPasswordResetEmail:catch", exception)
                _event.postValue(ForgotPasswordEvent.ShowError(exception))
            }.flowOn(Dispatchers.IO).firstOrNull()?.let {
                // TODO: fix result sendPasswordResetEmail()
                goBack()
            }
        }
    }

    // endregion
    // region Companion Object

    companion object {
        const val tag = "ForgotPasswordViewModel"
    }

    // endregion
    // region Inner Classes & Interfaces

    sealed class ForgotPasswordEvent {
        object GoBack : ForgotPasswordEvent()
        data class ShowError(val error: Throwable) : ForgotPasswordEvent()
    }

    // endregion
}