package kr.sul.servercore.command

import org.bukkit.World
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object KillAllCommand : Listener {
    @EventHandler(priority = EventPriority.LOW) // TODO: 아머스탠드 제거하는 것도 있어야 할 것 같긴함. 그래서 장식품으로 소환한 아머스탠드는 이름을 장식품으로 만들어주는게 좋을 듯
    // /<command> <all|named|drops|xp|mobs> [radius]
    fun onPlayerCommandPreprocess(e: PlayerCommandPreprocessEvent) {
        val p = e.player
        val args = e.message.split(" ").toTypedArray()
        val arg0 = args[0].toLowerCase()
        if (arg0 == "/killall" || arg0 == "/ekillall" || arg0 == "/remove" || arg0 == "/eremove" || arg0 == "/butcher" || arg0 == "/ebutcher" || arg0 == "/mobkill" || arg0 == "/emobkill") {
            e.isCancelled = true
            if (args.size == 1) {
                printHelpMessage(p)
                return
            }
            var radius = -1
            val removeType: RemoveType
            try {
                removeType = RemoveType.valueOf(args[1].toUpperCase())
                if (args.size >= 3) radius = args[2].toInt()
            } catch (ignored: Exception) {
                printHelpMessage(p)
                return
            }
            val removed = removeEntities(p.world, p, removeType, radius)
            p.sendMessage("§c§lKILLALL: §f엔티티 §e$removed§f마리를 제거하였습니다. §7[$removeType§7]")
        }
    }

    private fun printHelpMessage(p: Player) {
        p.sendMessage("")
        p.sendMessage("§c§lKILLALL: §f/killall <all|named|drops|xp|mobs> [radius]")
    }

    private fun removeEntities(world: World, p: Player, removeType: RemoveType, radius: Int): Int {
        var removed = 0
        for (entity in world.entities) {
            if (radius > 0) {
                if (p.player.location.distance(entity.location) > radius) continue
            }
            if (entity is HumanEntity || entity is ArmorStand || entity is ItemFrame || entity is Painting) continue
            when (removeType) {
                RemoveType.NAMED -> if (entity is LivingEntity && entity.getCustomName() != null) {
                    entity.remove()
                    removed++
                }
                RemoveType.DROPS -> if (entity is Item) {
                    entity.remove()
                    removed++
                }
                RemoveType.XP -> if (entity is ExperienceOrb) {
                    entity.remove()
                    removed++
                }
                RemoveType.MOBS -> if (entity is Animals || entity is NPC || entity is Snowman || entity is WaterMob
                        || entity is Monster || entity is ComplexLivingEntity || entity is Flying || entity is Slime || entity is Ambient) {
                    entity.remove()
                    removed++
                }
                RemoveType.ALL -> {
                    entity.remove()
                    removed++
                }
            }
        }
        return removed
    }

    private enum class RemoveType {
        DROPS, XP, MOBS, ALL, NAMED
    }
}