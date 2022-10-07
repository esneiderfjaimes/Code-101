package code101.framework.firebase

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import code101.domain.Auth
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {
    fun currentUser(): Auth?
    fun authWithGoogleIntent(context: Context): Intent?
    fun authWithGoogle(activityResult: ActivityResult): Flow<Result<Auth>>
    fun signOut()
}