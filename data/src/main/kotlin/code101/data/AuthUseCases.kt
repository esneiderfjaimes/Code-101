package code101.data

import android.content.Context
import androidx.activity.result.ActivityResult
import code101.domain.Auth
import code101.framework.firebase.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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

class AuthEmailAndPasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(email: String, password: String): Flow<Auth> {
        if (email.isBlank()) return emitThrow("Email is blank")
        if (password.isBlank()) return emitThrow("Password is blank")
        if (password.length < 6) return emitThrow("Password wrong")
        return repository.signInWithEmailAndPassword(email, password)
    }
}

class CreateEmailAuthPasswordUserCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(email: String, password: String, confirmPassword: String): Flow<Auth> {
        if (email.isBlank()) return emitThrow("Email is blank")
        if (password.isBlank()) return emitThrow("Password is blank")
        if (password.length < 6) return emitThrow("Password wrong")
        if (password != confirmPassword) return emitThrow("Confirm password wrong")
        return repository.createUserWithEmailAndPassword(email, password)
    }
}

class SendPasswordResetEmailUserCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke(email: String): Flow<Boolean> {
        if (email.isBlank()) return emitThrow("Email is blank")
        return repository.sendPasswordResetEmail(email)
    }
}

class LinkGoogleUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AuthRepository
) {
    val authWithGoogleIntent = repository.authWithGoogleIntent(context)

    fun invoke(result: ActivityResult) = repository.linkWithGoogle(result)
}

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    fun invoke() {
        repository.signOut()
    }
}
