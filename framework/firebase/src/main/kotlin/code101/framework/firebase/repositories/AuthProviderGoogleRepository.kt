package code101.framework.firebase.repositories

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import code101.framework.firebase.BuildConfig
import code101.framework.firebase.data_sources.AuthProviderGoogleDataSource
import code101.framework.firebase.utils.baseFlowAuth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthProviderGoogleRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    AuthProviderGoogleDataSource {

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
        firebaseAuth.signInWithCredential(googleCredential(activityResult)).await()
    }

    companion object {
        fun googleCredential(activityResult: ActivityResult): AuthCredential {
            val idToken = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)!!.idToken
            return GoogleAuthProvider.getCredential(idToken, null)
        }
    }
}