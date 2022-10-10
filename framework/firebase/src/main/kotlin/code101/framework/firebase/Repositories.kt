package code101.framework.firebase

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import code101.domain.Auth
import code101.framework.firebase.utils.UtilsException.Companion.toPrimitive
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias FlowAuth = suspend FlowCollector<Auth>.() -> AuthResult

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthDataSource {

    override fun currentUser(): Auth? {
        return firebaseAuth.currentUser?.toAuth()
    }

    override fun authWithGoogleIntent(context: Context): Intent? {
        return try {
            GoogleSignIn.getClient(
                context, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(BuildConfig.ServerClientId)
                    .requestEmail()
                    .build()
            ).signInIntent
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun authWithGoogle(activityResult: ActivityResult) = baseFlowAuth {
        val idToken = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            .getResult(ApiException::class.java)!!.idToken
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
            .await()
    }

    override fun signInWithEmailAndPassword(email: String, password: String) = baseFlowAuth {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    private fun baseFlowAuth(scope: FlowAuth): Flow<Auth> = flow {
        try {
            val auth = scope.invoke(this).toAuth() ?: throw Throwable("No data:[Auth]")
            emit(auth)
        } catch (e: Exception) {
            // exception transformation for use in higher modules
            throw Exception(e.message, e.cause?.toPrimitive())
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}