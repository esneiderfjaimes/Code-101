package code101.framework.firebase.repositories

import code101.framework.firebase.data_sources.AuthProviderPasswordDataSource
import code101.framework.firebase.utils.baseFlowAuth
import code101.framework.firebase.utils.baseFlowAuthVoid
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthProviderPasswordRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthProviderPasswordDataSource {

    override fun createUserWithEmailAndPassword(email: String, password: String) = baseFlowAuth {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override fun signInWithEmailAndPassword(email: String, password: String) = baseFlowAuth {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override fun updateEmail(email: String): Flow<Boolean> = baseFlowAuthVoid {
        firebaseAuth.currentUser!!.updateEmail(email)
    }

    override fun updatePassword(password: String) = baseFlowAuthVoid {
        firebaseAuth.currentUser!!.updatePassword(password)
    }

    override fun sendEmailVerification() = baseFlowAuthVoid {
        firebaseAuth.currentUser!!.sendEmailVerification()
    }

    override fun sendPasswordResetEmail(email: String): Flow<Boolean> = baseFlowAuthVoid {
        firebaseAuth.sendPasswordResetEmail(email)
    }
}