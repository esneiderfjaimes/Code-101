package code101.framework.firebase.repositories

import androidx.activity.result.ActivityResult
import code101.domain.Auth
import code101.framework.firebase.data_sources.AuthLinkMultipleProvidersDataSource
import code101.framework.firebase.utils.baseFlowAuth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class AuthLinkMultipleProvidersRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthLinkMultipleProvidersDataSource {

    override fun linkWithGoogle(activityResult: ActivityResult) = baseFlowAuth {
        val googleCredential = AuthProviderGoogleRepository.googleCredential(activityResult)
        firebaseAuth.currentUser?.linkWithCredential(googleCredential)
            ?.await() ?: throw Throwable("No data:[Auth]")
    }

    override fun unLink(providerId: String): Flow<Auth> = baseFlowAuth {
        firebaseAuth.currentUser!!.unlink(providerId).await()
    }
}