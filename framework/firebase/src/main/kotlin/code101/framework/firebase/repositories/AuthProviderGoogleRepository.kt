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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthProviderGoogleRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val firebaseAuth: FirebaseAuth
) :
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
        firebaseAuth.signInWithCredential(googleCredential(context, activityResult)).await()
    }

    companion object {
        private const val NAME_PROVIDER_GOOGLE = "auth_provider_google"
        private const val KEY_ID_TOKEN = "auth_provider_google"

        fun googleCredential(context: Context): AuthCredential {
            val sharedPref =
                context.getSharedPreferences(NAME_PROVIDER_GOOGLE, Context.MODE_PRIVATE)
            val googleIdToken = sharedPref.getString(KEY_ID_TOKEN, null)
            return GoogleAuthProvider.getCredential(googleIdToken, null)
        }

        fun googleCredential(context: Context, activityResult: ActivityResult): AuthCredential {
            val idToken = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
                .getResult(ApiException::class.java)!!.idToken

            with(context.getSharedPreferences(NAME_PROVIDER_GOOGLE, Context.MODE_PRIVATE).edit()) {
                putString(KEY_ID_TOKEN, idToken)
                apply()
            }

            return GoogleAuthProvider.getCredential(idToken, null)
        }
    }
}