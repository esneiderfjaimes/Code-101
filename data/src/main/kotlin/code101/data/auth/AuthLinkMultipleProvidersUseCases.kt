package code101.data.auth

import android.content.Context
import androidx.activity.result.ActivityResult
import code101.framework.firebase.data_sources.AuthLinkMultipleProvidersDataSource
import code101.framework.firebase.data_sources.AuthProviderGoogleDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LinkGoogleUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val repository: AuthLinkMultipleProvidersDataSource,
    repositoryProviderGoogle: AuthProviderGoogleDataSource
) {
    val authWithGoogleIntent = repositoryProviderGoogle.authWithGoogleIntent(context)

    fun invoke(result: ActivityResult) = repository.linkWithGoogle(result)
}

class UnLinkUseCase @Inject constructor(private val repository: AuthLinkMultipleProvidersDataSource) {
    fun invoke(providerId: String) = repository.unLink(providerId)
}