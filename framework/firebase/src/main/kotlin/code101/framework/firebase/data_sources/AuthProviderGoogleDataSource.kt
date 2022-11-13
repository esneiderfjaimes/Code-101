package code101.framework.firebase.data_sources

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import code101.domain.Auth
import kotlinx.coroutines.flow.Flow

interface AuthProviderGoogleDataSource {
    fun authWithGoogleIntent(context: Context): Intent?
    fun authWithGoogle(activityResult: ActivityResult): Flow<Auth>
}