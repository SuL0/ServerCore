package kr.sul.servercore.extensionfunction

import org.bukkit.block.BlockFace
import org.bukkit.entity.Entity

object EntityDirection {
    fun Entity.getFacingDirection(): BlockFace {
        var yaw: Float = this.location.yaw
        if (yaw < 0.0f) yaw += 180.0f
        if (yaw in 45.0f..135.0f) return BlockFace.EAST
        if (yaw in 135.0f..225.0f) return BlockFace.SOUTH
        return if (yaw in 225.0f..315.0f) BlockFace.WEST else BlockFace.NORTH
    }
}