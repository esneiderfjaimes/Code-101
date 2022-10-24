package red.code101.app.ui.auth.register

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.CreateEmailAuthPasswordUserCase
import code101.domain.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createEmailAuthPassword: CreateEmailAuthPasswordUserCase
) : ViewModel() {
    // region Fields

    private val _event = MutableLiveData<RegisterEvent>()
    val event: LiveData<RegisterEvent> get() = _event

    // endregion
    // region Public Methods

    fun goBack(resultIsCancel: Boolean = true) {
        _event.postValue(RegisterEvent.GoBack(resultIsCancel))
    }

    fun goLogin() {
        _event.postValue(RegisterEvent.GoLogin)
    }

    fun registerEmailAndPassword(
        email: Editable?,
        password: Editable?,
        confirmPassword: Editable?
    ) {
        viewModelScope.launch {
            createEmailAuthPassword.invoke(
                email = email.toString().trim(),
                password = password.toString().trim(),
                confirmPassword = confirmPassword.toString().trim()
            ).catch { exception ->
                Log.e(tag, "registerEmailAndPassword:catch", exception)
                _event.postValue(RegisterEvent.ShowError(exception))
            }.flowOn(Dispatchers.IO).firstOrNull()?.let { auth ->
                _event.postValue(RegisterEvent.SuccessfulAuth(auth))
            }
        }
    }

    // endregion
    // region Companion Object

    companion object {
        const val tag = "RegisterViewModel"
    }

    // endregion
    // region Inner Classes & Interfaces

    sealed class RegisterEvent {
        data class GoBack(val resultIsCancel: Boolean = true) : RegisterEvent()
        object GoLogin : RegisterEvent()
        data class SuccessfulAuth(val auth: Auth) : RegisterEvent()
        data class ShowError(val error: Throwable) : RegisterEvent()
    }

    // endregion
}