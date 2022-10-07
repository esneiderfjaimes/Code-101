package code101.framework.firebase

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import code101.domain.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

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

    override fun authWithGoogle(activityResult: ActivityResult): Flow<Result<Auth>> = flow {
        try {
            emit(Result.success(userByActivityResult(activityResult)))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    private suspend fun userByActivityResult(result: ActivityResult): Auth {
        val idToken = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            .getResult(ApiException::class.java)!!.idToken
        val authResult =
            firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .await()
        return authResult.toAuth() ?: throw Throwable("No data:[User]")
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}