package kr.sul.servercore.util

object ObjectInitializer {
    fun <T> forceInit(clazz: Class<T>): Class<T>? {
        try {
            Class.forName(clazz.name, true, clazz.classLoader)
        } catch (e: ClassNotFoundException) {
            throw AssertionError(e) // Can't happen
        }
        return clazz
    }
}