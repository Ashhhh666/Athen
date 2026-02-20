@file:Suppress("unused")

package xyz.aerii.athen.modules.impl.general

import net.minecraft.sounds.SoundEvent
import xyz.aerii.athen.annotations.Load
import xyz.aerii.athen.config.Category
import xyz.aerii.athen.events.SoundPlayEvent
import xyz.aerii.athen.handlers.Smoothie.play
import xyz.aerii.athen.modules.Module
import xyz.aerii.athen.utils.sound
import xyz.aerii.athen.utils.url

@Load
object SoundReplacer : Module(
    "Sound replacer",
    "Replaces all sounds, with a sound that you select.",
    Category.GENERAL
) {
    private var real: SoundEvent? = null
    private val sound = config.textInput("Sound", "entity.cat.purreow").custom("sound")
    private val pitch by config.slider("Pitch", 1f, 0f, 1f, showDouble = true)
    private val volume by config.slider("Volume", 1f, 0f, 1f, showDouble = true)

    private val _unused by config.button("Play") {
        real?.play(volume, pitch)
    }

    private val _unused0 by config.button("Open sounds list") {
        "https://www.digminecraft.com/lists/sound_list_pc.php".url()
    }

    init {
        real = sound.value.sound()

        sound.state.onChange {
            real = it.sound()
        }

        on<SoundPlayEvent> {
            val r = real?.takeIf { it != sound } ?: return@on

            cancel()
            r.play(this@SoundReplacer.volume, this@SoundReplacer.pitch)
        }
    }
}