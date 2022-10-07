package red.code101.app.ui.login

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.AuthGoogleUseCase
import code101.data.CurrentAuthUseCase
import code101.domain.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    currentAuth: CurrentAuthUseCase,
    private val authGoogle: AuthGoogleUseCase,
) : ViewModel() {

    // region Fields

    private var result: ActivityResultLauncher<Intent>? = null
    private val _event = MutableLiveData<LoginEvent>()
    val event: LiveData<LoginEvent> get() = _event

    // endregion

    // region Override Methods & Callbacks

    init {
        currentAuth.invoke()?.let { auth ->
            _event.postValue(LoginEvent.SuccessfulAuth(auth))
        }
    }

    // endregion

    // region Public Methods

    fun initAuthWithGoogle(fragment: Fragment) {
        result =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                authWithGoogle(it)
            }
    }

    fun launchSignInGoogle() {
        result?.launch(authGoogle.authWithGoogleIntent)
    }

    private fun authWithGoogle(result: ActivityResult) {
        viewModelScope.launch {
            authGoogle.invoke(result).catch { exception ->
                Log.e(tag, "authWithGoogle:catch", exception)
                _event.postValue(LoginEvent.ShowError(exception))
            }.collect { userResult ->
                _event.postValue(LoginEvent.SuccessfulAuth(userResult.getOrThrow()))
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
        data class SuccessfulAuth(val auth: Auth) : LoginEvent()
        data class ShowError(val error: Throwable) : LoginEvent()
        object SignUp : LoginEvent()
    }

    // endregion

}