package xyz.aerii.athen.modules

import net.minecraft.network.protocol.Packet
import xyz.aerii.athen.annotations.OnlyIn
import xyz.aerii.athen.annotations.Redstone
import xyz.aerii.athen.api.dungeon.DungeonAPI
import xyz.aerii.athen.api.location.LocationAPI
import xyz.aerii.athen.config.Category
import xyz.aerii.athen.config.ConfigBuilder
import xyz.aerii.athen.events.PacketEvent
import xyz.aerii.athen.events.core.Event
import xyz.aerii.athen.events.core.runWhen
import xyz.aerii.athen.handlers.React
import xyz.aerii.athen.handlers.React.Companion.and
import xyz.aerii.athen.utils.ALWAYS_TRUE
import xyz.aerii.athen.utils.toCamelCase
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

open class Module(
    name: String? = null,
    description: String? = null,
    category: Category? = null,
    default: Boolean = false
) {
    private var _config: ConfigBuilder? = null
    private val _location = run {
        val onlyIn = this::class.findAnnotation<OnlyIn>() ?: return@run ALWAYS_TRUE
        when {
            onlyIn.floors.isNotEmpty() -> DungeonAPI.floor.map { it in onlyIn.floors }
            onlyIn.areas.isNotEmpty() -> LocationAPI.area.map { it in onlyIn.areas }
            onlyIn.islands.isNotEmpty() -> LocationAPI.island.map { it in onlyIn.islands }
            onlyIn.skyblock -> LocationAPI.isOnSkyBlock
            else -> ALWAYS_TRUE
        }
    }

    val configKey = name?.toCamelCase()
    val redstone = this::class.hasAnnotation<Redstone>()
    val react: React<Boolean> by lazy { if (redstone) ALWAYS_TRUE else config.state and _location }
    val config: ConfigBuilder
        get() = _config ?: error("Config not initialized")

    init {
        run {
            _config = ConfigBuilder(
                configKey ?: return@run,
                name ?: return@run,
                description ?: return@run,
                category ?: return@run,
                default
            ).also { it.module = this }
        }
    }

    protected inline fun <reified T : Event> on(
        priority: Int = 0,
        noinline handler: T.() -> Unit
    ) = xyz.aerii.athen.events.core.on<T>(priority, handler).runWhen(react)

    protected inline fun <reified E : PacketEvent, reified P : Packet<*>> on(
        priority: Int = 0,
        noinline handler: P.(E) -> Unit
    ) = xyz.aerii.athen.events.core.on<E, P>(priority, handler).runWhen(react)
}