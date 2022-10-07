package code101.data

import android.content.Context
import androidx.activity.result.ActivityResult
import code101.framework.firebase.AuthRepository
import code101.domain.Auth
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CurrentAuthUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): Auth? = repository.currentUser()
}

class IsLoginUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(): Boolean = repository.currentUser() != null
}

class AuthGoogleUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AuthRepository
) {
    val authWithGoogleIntent = repository.authWithGoogleIntent(context)

    fun invoke(result: ActivityResult) = repository.authWithGoogle(result)
}

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke() {
        repository.signOut()
    }
}
