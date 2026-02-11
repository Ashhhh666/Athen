package xyz.aerii.athen.api.kuudra.enums

import net.minecraft.world.entity.Entity
import xyz.aerii.athen.Athen
import xyz.aerii.athen.handlers.Schrodinger
import xyz.aerii.athen.handlers.Smoothie.client
import xyz.aerii.athen.handlers.Typo.stripped

class KuudraPlayer(
    val name: String
) {
    var deaths = 0
        internal set

    val entity by Schrodinger(::d) { !it.isAlive }

    init {
        Athen.LOGGER.debug("Created KuudraPlayer with entity: {}", entity)
    }

    private fun d(): Entity? =
        client.level?.players()?.find { it.uuid.version() == 4 && it.name.stripped() == name }

    override fun toString(): String =
        "KuudraPlayer(n=$name, d=$deaths, entity: ${entity != null})"
}