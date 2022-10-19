package red.code101.app.ui.login

import android.content.Intent
import android.text.Editable
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.AuthEmailAndPasswordUseCase
import code101.data.AuthGoogleUseCase
import code101.data.CurrentAuthUseCase
import code101.domain.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val currentAuth: CurrentAuthUseCase,
    private val authGoogle: AuthGoogleUseCase,
    private val authEmailAndPassword: AuthEmailAndPasswordUseCase,
) : ViewModel() {

    // region Fields

    private var result: ActivityResultLauncher<Intent>? = null
    private val _event = MutableLiveData<LoginEvent>()
    val event: LiveData<LoginEvent> get() = _event

    val needInflate get() = getAuth() == null

    // endregion

    // region Public Methods

    @Singleton
    fun getAuth() = currentAuth.invoke()

    fun initAuthWithGoogle(fragment: Fragment) {
        result =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                authWithGoogle(it)
            }
    }

    fun launchSignInGoogle() {
        result?.launch(authGoogle.authWithGoogleIntent)
    }

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

    private fun authWithGoogle(result: ActivityResult) {
        launchFlowAuth(
            flowResultAuth = authGoogle.invoke(result),
            nameFunc = "authWithGoogle"
        )
    }

    fun authEmailAndPassword(email: Editable?, password: Editable?) {
        if (email.isNullOrBlank()) return
        if (password.isNullOrBlank()) return

        launchFlowAuth(
            flowResultAuth = authEmailAndPassword.invoke(email.toString(), password.toString()),
            nameFunc = "authEmailAndPassword"
        )
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