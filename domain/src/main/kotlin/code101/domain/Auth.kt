package code101.domain

data class Auth(
    val uid: String?,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    val authProviders: List<AuthProvider> = emptyList()
)

data class AuthProvider(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
)