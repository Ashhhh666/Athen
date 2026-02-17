package xyz.aerii.athen.events.core

import net.minecraft.network.protocol.Packet
import xyz.aerii.athen.events.CommandRegistration
import xyz.aerii.athen.events.PacketEvent
import xyz.aerii.athen.handlers.React

inline fun <reified T : Event> on(
    priority: Int = 0,
    noinline handler: T.() -> Unit
) = EventBus.on(T::class.java, priority, handler)

inline fun <reified E : PacketEvent, reified P : Packet<*>> on(
    priority: Int = 0,
    noinline handler: P.(E) -> Unit
): Node<*> {
    return on<E>(priority) {
        (packet as? P)?.handler(this)
    }
}

fun Node<*>.runWhen(state: React<Boolean>) = apply {
    if (!overridden && eventClass != CommandRegistration::class.java) add(state)
}

fun Node<*>.override(state: React<Boolean>) = apply {
    overridden = true
    conditions.clear()
    add(state)
}

fun Node<*>.override() = apply {
    overridden = true
    conditions.clear()
    register()
}

private fun Node<*>.add(state: React<Boolean>) = apply {
    conditions.add(state)
    state.onChange { evaluate() }
    evaluate()
}