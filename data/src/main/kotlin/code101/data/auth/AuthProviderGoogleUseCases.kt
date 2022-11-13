package code101.data.auth

import android.content.Context
import androidx.activity.result.ActivityResult
import code101.framework.firebase.data_sources.AuthProviderGoogleDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthGoogleUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AuthProviderGoogleDataSource
) {
    val authWithGoogleIntent = repository.authWithGoogleIntent(context)

    fun invoke(result: ActivityResult) = repository.authWithGoogle(result)
}
