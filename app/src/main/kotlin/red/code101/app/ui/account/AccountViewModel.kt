package red.code101.app.ui.account

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code101.data.CurrentAuthUseCase
import code101.data.LinkGoogleUseCase
import code101.data.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import red.code101.app.ui.account.AccountViewModel.AccountEvent.SignOut
import red.code101.app.ui.auth.LinkProvider
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val currentUser: CurrentAuthUseCase,
    private val linkGoogleUseCase: LinkGoogleUseCase,
    private val signOut: SignOutUseCase
) : ViewModel() {
    // region Fields

    private var result: ActivityResultLauncher<Intent>? = null

    private val _event = MutableLiveData<AccountEvent>()
    val event: LiveData<AccountEvent> get() = _event

    val user get() = currentUser.invoke()

    // endregion
    // region Public Methods

    fun initAuthWithGoogle(fragment: Fragment) {
        result =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                viewModelScope.launch {
                    linkGoogleUseCase.invoke(it).catch { exception ->
                        Log.e(tag, "initAuthWithGoogle:catch", exception)
                        _event.postValue(AccountEvent.ShowError(exception))
                    }.flowOn(Dispatchers.IO).firstOrNull()
                }
            }
    }

    fun signOut() {
        signOut.invoke()
        _event.postValue(SignOut)
    }

    fun onProviderClick(linkProvider: LinkProvider) {
        when (linkProvider) {
            LinkProvider.EmailAndPassword ->
                _event.postValue(AccountEvent.ShowSnack("TODO: link email and password"))
            LinkProvider.Google -> launchSignInGoogle()
            LinkProvider.Facebook ->
                _event.postValue(AccountEvent.ShowSnack("TODO: link facebook"))
        }
    }

    // endregion
    // region Private Methods

    private fun launchSignInGoogle() {
        result?.launch(linkGoogleUseCase.authWithGoogleIntent)
    }

    // endregion
    // region Companion Object

    companion object {
        const val tag = "AccountViewModel"
    }

    // endregion
    // region Inner Classes & Interfaces

    sealed class AccountEvent {
        data class ShowError(val error: Throwable) : AccountEvent()
        data class ShowSnack(val msg: String) : AccountEvent()
        object SignOut : AccountEvent()
    }

    // endregion
}