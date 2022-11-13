package red.code101.app.ui.auth.login

import android.content.Intent
import android.text.Editable
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.auth.AuthEmailAndPasswordUseCase
import code101.data.auth.AuthGoogleUseCase
import code101.domain.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authGoogle: AuthGoogleUseCase,
    private val authEmailAndPassword: AuthEmailAndPasswordUseCase
) : ViewModel() {
    // region Fields

    private var result: ActivityResultLauncher<Intent>? = null
    private val _event = MutableLiveData<LoginEvent>()
    val event: LiveData<LoginEvent> get() = _event

    // endregion
    // region Public Methods

    fun initAuthWithGoogle(fragment: Fragment) {
        result =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                launchFlowAuth(
                    flowResultAuth = authGoogle.invoke(it),
                    nameFunc = "authWithGoogle"
                )
            }
    }

    fun goBack(resultIsCancel: Boolean = true) {
        _event.postValue(LoginEvent.GoBack(resultIsCancel))
    }

    fun goRegister() {
        _event.postValue(LoginEvent.GoRegister)
    }

    fun goForgotPassword() {
        _event.postValue(LoginEvent.ShowForgotPassword)
    }

    fun launchSignInGoogle() {
        result?.launch(authGoogle.authWithGoogleIntent)
    }

    fun authEmailAndPassword(email: Editable?, password: Editable?) {
        launchFlowAuth(
            flowResultAuth = authEmailAndPassword.invoke(
                email = email.toString().trim(),
                password = password.toString().trim()
            ),
            nameFunc = "authEmailAndPassword"
        )
    }

    // endregion
    // region Private Methods

    private fun launchFlowAuth(
        flowResultAuth: Flow<Auth>,
        nameFunc: String,
    ) {
        viewModelScope.launch {
            flowResultAuth.catch { exception ->
                Log.e(tag, "$nameFunc:catch", exception)
                _event.postValue(LoginEvent.ShowError(exception))
            }.flowOn(Dispatchers.IO).firstOrNull()?.let { auth ->
                _event.postValue(LoginEvent.SuccessfulAuth(auth))
            }
        }
    }

    // endregion
    // region Companion Object

    companion object {
        const val tag = "LoginFragment"
    }

    // endregion
    // region Inner Classes & Interfaces

    sealed class LoginEvent {
        data class GoBack(val resultIsCancel: Boolean = true) : LoginEvent()
        object GoRegister : LoginEvent()
        object ShowForgotPassword : LoginEvent()
        data class SuccessfulAuth(val auth: Auth) : LoginEvent()
        data class ShowError(val error: Throwable) : LoginEvent()
    }

    // endregion
}