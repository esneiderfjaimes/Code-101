package code101.framework.firebase.repositories

import android.net.Uri
import code101.domain.Auth
import code101.framework.firebase.data_sources.AuthManageUsersDataSource
import code101.framework.firebase.utils.baseFlowAuthVoid
import code101.framework.firebase.utils.toAuth
import code101.framework.firebase.utils.userProfileChangeRequest
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthManageUsersRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthManageUsersDataSource {

    override fun currentUser(): Auth? {
        return firebaseAuth.currentUser?.toAuth()
    }

    override fun updateProfile(displayName: String?, photoUri: Uri?) = baseFlowAuthVoid {
        val user = firebaseAuth.currentUser
        val userProfile = userProfileChangeRequest {
            this.displayName = displayName
            this.photoUri = photoUri
        }
        user!!.updateProfile(userProfile)
    }

    override fun reauthenticate(authCredential: AuthCredential) = baseFlowAuthVoid {
        firebaseAuth.currentUser!!.reauthenticate(authCredential)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun delete(): Flow<Boolean> = baseFlowAuthVoid {
        firebaseAuth.currentUser!!.delete()
    }
}