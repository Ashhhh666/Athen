@file:Suppress("PropertyName")

package xyz.aerii.athen.handlers

class Schrodinger<T>(
    private val fn: () -> T?,
    immediate: Boolean = false,
    private val predicate: (T) -> Boolean = { false },
) : Lazy<T?> {
    var _v: T? = null

    override val value: T?
        get() {
            val v = _v
            if (v != null && !predicate(v)) return v

            return fn().also { _v = it }
        }

    init {
        if (immediate) _v = fn()
    }

    override fun isInitialized() = _v != null
}