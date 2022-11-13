package code101.framework.firebase.di

import code101.framework.firebase.data_sources.AuthLinkMultipleProvidersDataSource
import code101.framework.firebase.data_sources.AuthManageUsersDataSource
import code101.framework.firebase.data_sources.AuthProviderGoogleDataSource
import code101.framework.firebase.data_sources.AuthProviderPasswordDataSource
import code101.framework.firebase.repositories.AuthLinkMultipleProvidersRepository
import code101.framework.firebase.repositories.AuthManageUsersRepository
import code101.framework.firebase.repositories.AuthProviderGoogleRepository
import code101.framework.firebase.repositories.AuthProviderPasswordRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    internal fun provideManageUsers(repository: AuthManageUsersRepository): AuthManageUsersDataSource {
        return repository
    }

    @Provides
    @Singleton
    internal fun provideProviderGoogle(repository: AuthProviderGoogleRepository): AuthProviderGoogleDataSource {
        return repository
    }

    @Provides
    @Singleton
    internal fun provideProviderPassword(repository: AuthProviderPasswordRepository): AuthProviderPasswordDataSource {
        return repository
    }

    @Provides
    @Singleton
    internal fun provideLinkMultipleProviders(repository: AuthLinkMultipleProvidersRepository): AuthLinkMultipleProvidersDataSource {
        return repository
    }
}