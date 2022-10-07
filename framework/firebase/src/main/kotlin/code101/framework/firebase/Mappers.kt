package code101.framework.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import code101.domain.Auth

fun AuthResult.toAuth() = user?.toAuth()

fun FirebaseUser.toAuth() = Auth(
    uid = uid,
    name = displayName,
    email = email,
    photoUrl = photoUrl?.toString()
)