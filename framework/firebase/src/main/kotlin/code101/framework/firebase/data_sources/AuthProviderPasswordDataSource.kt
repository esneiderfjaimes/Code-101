package code101.framework.firebase.data_sources

import code101.domain.Auth
import kotlinx.coroutines.flow.Flow

interface AuthProviderPasswordDataSource {
    /**
     * Create a password-based account
     */
    fun createUserWithEmailAndPassword(email: String, password: String): Flow<Auth>

    /**
     * Sign in a user with an email address and password
     */
    fun signInWithEmailAndPassword(email: String, password: String): Flow<Auth>

    /**
     * Set or updated a user's email address
     */
    fun updateEmail(email: String): Flow<Boolean>

    /**
     * Set or updated a user's password
     */
    fun updatePassword(password: String): Flow<Boolean>

    /**
     * Send a user a verification email
     */
    fun sendEmailVerification(): Flow<Boolean>

    /**
     * Send a password reset email
     */
    fun sendPasswordResetEmail(email: String): Flow<Boolean>
}