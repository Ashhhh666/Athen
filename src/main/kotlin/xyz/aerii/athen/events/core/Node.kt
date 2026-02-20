package xyz.aerii.athen.events.core

import xyz.aerii.athen.handlers.React

class Node<T : Event>(
    @JvmField val eventClass: Class<T>,
    @JvmField var handler: (T) -> Unit,
    @JvmField val priority: Int
) {
    @Volatile var registered = false
    internal var overridden = false
    internal val conditions = mutableListOf<React<Boolean>>()

    fun once() = apply {
        val original = handler
        handler = {
            original(it)
            unregister()
        }
    }

    fun register(): Boolean {
        if (registered) return false
        registered = true
        EventBus.add(this)
        return true
    }

    fun unregister(): Boolean {
        if (!registered) return false
        registered = false
        EventBus.remove(this)
        return true
    }

    internal fun evaluate() = if (conditions.all { it.value }) register() else unregister()
}