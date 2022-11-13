package code101.framework.firebase.data_sources

import androidx.activity.result.ActivityResult
import code101.domain.Auth
import kotlinx.coroutines.flow.Flow

interface AuthLinkMultipleProvidersDataSource {
    /**
     * Link with Google
     */
    fun linkWithGoogle(activityResult: ActivityResult): Flow<Auth>

    /**
     * Unlink an auth provider from a user account
     */
    fun unLink(providerId: String): Flow<Auth>
}