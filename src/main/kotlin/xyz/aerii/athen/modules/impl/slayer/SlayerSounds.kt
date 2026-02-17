package xyz.aerii.athen.modules.impl.slayer

import net.minecraft.sounds.SoundEvents
import xyz.aerii.athen.annotations.Load
import xyz.aerii.athen.annotations.OnlyIn
import xyz.aerii.athen.config.Category
import xyz.aerii.athen.events.SoundPlayEvent
import xyz.aerii.athen.modules.Module

@Load
@OnlyIn(skyblock = true)
object SlayerSounds : Module(
    "Slayer sounds",
    "Toggles for slayer sounds!",
    Category.SLAYER
) {
    private val disableEnder by config.switch("Disable voidgloom sounds", true)
    private val enderSet = setOf(SoundEvents.ENDERMAN_STARE, SoundEvents.ENDERMAN_SCREAM)

    init {
        on<SoundPlayEvent> {
            if (!disableEnder) return@on
            if (sound in enderSet) cancel()
        }
    }
}