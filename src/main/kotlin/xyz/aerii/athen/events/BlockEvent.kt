package xyz.aerii.athen.events

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import xyz.aerii.athen.events.core.Event

data class BlockEvent(
    val old: BlockState,
    val new: BlockState,
    val pos: BlockPos
) : Event()