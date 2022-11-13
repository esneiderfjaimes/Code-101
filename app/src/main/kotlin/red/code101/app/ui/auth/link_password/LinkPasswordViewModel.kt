package red.code101.app.ui.auth.link_password

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.auth.LinkPasswordUseCase
import code101.data.auth.SendPasswordResetEmailUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import red.code101.app.ui.auth.register.RegisterViewModel
import javax.inject.Inject

@HiltViewModel
class LinkPasswordViewModel @Inject constructor(
    private val linkPasswordUseCase: LinkPasswordUseCase
) : ViewModel() {
    // region Fields

    private val _event = MutableLiveData<LinkPasswordEvent>()
    val event: LiveData<LinkPasswordEvent> get() = _event

    // endregion
    // region Public Methods

    fun goBack() {
        _event.postValue(LinkPasswordEvent.GoBack)
    }

    fun registerEmailAndPassword(
        email: Editable?,
        password: Editable?,
        confirmPassword: Editable?
    ) {
        viewModelScope.launch {
            linkPasswordUseCase.invoke(
                email = email.toString().trim(),
                password = password.toString().trim(),
                confirmPassword = confirmPassword.toString().trim()
            ).catch { exception ->
                Log.e(RegisterViewModel.tag, "registerEmailAndPassword:catch", exception)
                _event.postValue(LinkPasswordEvent.ShowError(exception))
                _event.postValue(LinkPasswordEvent.GoBack)
            }.flowOn(Dispatchers.IO).firstOrNull()?.let {
                _event.postValue(LinkPasswordEvent.GoBack)
            }
        }
    }


    // endregion
    // region Companion Object

    companion object {
        const val tag = "LinkPasswordViewModel"
    }

    // endregion
    // region Inner Classes & Interfaces

    sealed class LinkPasswordEvent {
        object GoBack : LinkPasswordEvent()
        data class ShowError(val error: Throwable) : LinkPasswordEvent()
    }

    // endregion
}