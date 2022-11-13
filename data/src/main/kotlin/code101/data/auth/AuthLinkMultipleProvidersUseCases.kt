package code101.data.auth

import android.content.Context
import androidx.activity.result.ActivityResult
import code101.data.emitThrow
import code101.domain.Auth
import code101.framework.firebase.data_sources.AuthLinkMultipleProvidersDataSource
import code101.framework.firebase.data_sources.AuthProviderGoogleDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LinkPasswordUseCase @Inject constructor(private val repository: AuthLinkMultipleProvidersDataSource) {
    fun invoke(email: String, password: String, confirmPassword: String): Flow<Auth> {
        if (email.isBlank()) return emitThrow("Email is blank")
        if (password.isBlank()) return emitThrow("Password is blank")
        if (password.length < 6) return emitThrow("Password wrong")
        if (password != confirmPassword) return emitThrow("Confirm password wrong")
        return repository.linkWithPassword(email, password)
    }
}

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