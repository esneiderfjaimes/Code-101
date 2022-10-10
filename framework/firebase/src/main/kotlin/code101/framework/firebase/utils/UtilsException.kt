package code101.framework.firebase.utils

class UtilsException {
    companion object {
        @JvmStatic
        fun Throwable.toPrimitive(): Throwable {
            return Throwable(message, cause?.toPrimitive())
        }
    }
}