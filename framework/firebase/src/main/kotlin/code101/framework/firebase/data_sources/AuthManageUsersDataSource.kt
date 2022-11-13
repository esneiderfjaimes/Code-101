package code101.framework.firebase.data_sources

import android.net.Uri
import code101.domain.Auth
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthManageUsersDataSource {
    /**
     * Get a user's profile
     */
    fun currentUser(): Auth?

    /**
     * Update a user's profile
     */
    fun updateProfile(displayName: String?, photoUri: Uri?): Flow<Boolean>

    /**
     * Re-authenticate a user
     */
    fun reauthenticate(authCredential: AuthCredential): Flow<Boolean>

    /**
     * Sign out
     */
    fun signOut()

    /**
     * Delete a user
     */
    fun delete(): Flow<Boolean>
}