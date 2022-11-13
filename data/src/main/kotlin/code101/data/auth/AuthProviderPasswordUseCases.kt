package code101.data.auth

import code101.data.emitThrow
import code101.domain.Auth
import code101.framework.firebase.data_sources.AuthProviderPasswordDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthEmailAndPasswordUseCase @Inject constructor(private val repository: AuthProviderPasswordDataSource) {
    fun invoke(email: String, password: String): Flow<Auth> {
        if (email.isBlank()) return emitThrow("Email is blank")
        if (password.isBlank()) return emitThrow("Password is blank")
        if (password.length < 6) return emitThrow("Password wrong")
        return repository.signInWithEmailAndPassword(email, password)
    }
}

class CreateEmailAuthPasswordUserCase @Inject constructor(private val repository: AuthProviderPasswordDataSource) {
    fun invoke(email: String, password: String, confirmPassword: String): Flow<Auth> {
        if (email.isBlank()) return emitThrow("Email is blank")
        if (password.isBlank()) return emitThrow("Password is blank")
        if (password.length < 6) return emitThrow("Password wrong")
        if (password != confirmPassword) return emitThrow("Confirm password wrong")
        return repository.createUserWithEmailAndPassword(email, password)
    }
}

class SendPasswordResetEmailUserCase @Inject constructor(private val repository: AuthProviderPasswordDataSource) {
    fun invoke(email: String): Flow<Boolean> {
        if (email.isBlank()) return emitThrow("Email is blank")
        return repository.sendPasswordResetEmail(email)
    }
}