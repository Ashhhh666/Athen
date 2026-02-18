package xyz.aerii.athen.api.kuudra.enums

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.monster.Giant
import net.minecraft.world.phys.AABB
import xyz.aerii.athen.utils.markerAABB
import kotlin.math.cos
import kotlin.math.sin

class AbstractSupply(val entity: Giant) {
    private var x0: Double = 0.0
    private var z0: Double = 0.0

    private val x: Int
        get() = (entity.x + x0).toInt()

    private val z: Int
        get() = (entity.z + z0).toInt()

    val blockPos: BlockPos
        get() = BlockPos(x, 75, z)

    val aabb: AABB
        get() = blockPos.markerAABB().inflate(1.0)

    init {
        val rad = Math.toRadians(entity.yRot + 130.0)
        x0 = 3.75 * cos(rad)
        z0 = 3.75 * sin(rad)
    }
}