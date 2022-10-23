package code101.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> emitThrow(msg: String): Flow<T> = flow {
    throw Throwable(msg)
}