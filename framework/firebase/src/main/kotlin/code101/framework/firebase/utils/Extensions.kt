package code101.framework.firebase.utils

import code101.domain.Auth
import code101.framework.firebase.utils.UtilsException.Companion.toPrimitive
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun userProfileChangeRequest(builder: UserProfileChangeRequest.Builder.() -> Unit): UserProfileChangeRequest {
    return UserProfileChangeRequest.Builder().apply(builder).build()
}

typealias FlowAuth = suspend FlowCollector<Auth>.() -> AuthResult
typealias FlowAuthVoid = suspend FlowCollector<Boolean>.() -> Task<Void>

fun baseFlowAuth(scope: FlowAuth): Flow<Auth> = flow {
    try {
        val auth = scope.invoke(this).toAuth() ?: throw Throwable("No data:[Auth]")
        emit(auth)
    } catch (e: Exception) {
        // exception transformation for use in higher modules
        throw Exception(e.message, e.cause?.toPrimitive())
    }
}

fun baseFlowAuthVoid(scope: FlowAuthVoid): Flow<Boolean> = flow {
    try {
        emit(scope.invoke(this).isSuccessful)
    } catch (e: Exception) {
        // exception transformation for use in higher modules
        throw Exception(e.message, e.cause?.toPrimitive())
    }
}