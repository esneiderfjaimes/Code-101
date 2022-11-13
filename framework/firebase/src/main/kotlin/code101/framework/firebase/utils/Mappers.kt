package code101.framework.firebase.utils

import code101.domain.Auth
import code101.domain.AuthProvider
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

fun AuthResult.toAuth() = user?.toAuth()

fun FirebaseUser.toAuth() = Auth(
    uid = uid,
    name = displayName,
    email = email,
    photoUrl = photoUrl?.toString()?.replace("=s96-c", ""),
    authProviders = providerData.run {
        removeIf { it.providerId == "firebase" }
        map { it.toProvider() }
    }
)

fun UserInfo.toProvider() = AuthProvider(
    id = providerId,
    displayName = displayName,
    email = email,
    photoUrl = photoUrl?.toString()
)