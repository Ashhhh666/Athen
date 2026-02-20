package xyz.aerii.athen.utils

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

fun String.sound(): SoundEvent? {
    val p = ResourceLocation.tryParse(this) ?: return null
    return SoundEvent.createVariableRangeEvent(p)
}