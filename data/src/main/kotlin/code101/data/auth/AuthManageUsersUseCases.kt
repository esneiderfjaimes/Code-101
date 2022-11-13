package code101.data.auth

import code101.domain.Auth
import code101.framework.firebase.repositories.AuthManageUsersRepository
import javax.inject.Inject

class CurrentAuthUseCase @Inject constructor(private val repository: AuthManageUsersRepository) {
    fun invoke(): Auth? = repository.currentUser()
}

class IsLoginUseCase @Inject constructor(private val repository: AuthManageUsersRepository) {
    fun invoke(): Boolean = repository.currentUser() != null
}

class SignOutUseCase @Inject constructor(private val repository: AuthManageUsersRepository) {
    fun invoke() {
        repository.signOut()
    }
}