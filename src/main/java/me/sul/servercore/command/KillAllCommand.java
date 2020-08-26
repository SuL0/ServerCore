package me.sul.servercore.command;

import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class KillAllCommand implements Listener {
    @EventHandler(priority = EventPriority.LOW)
    // TODO: 아머스탠드 제거하는 것도 있어야 할 것 같긴함. 그래서 장식품으로 소환한 아머스탠드는 이름을 장식품으로 만들어주는게 좋을 듯
    // /<command> <all|named|drops|xp|mobs> [radius]
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String args[] = e.getMessage().split(" ");
        String arg0 = args[0].toLowerCase();
        if (arg0.equals("/killall") || arg0.equals("/ekillall") || arg0.equals("/remove") || arg0.equals("/eremove") || arg0.equals("/butcher") || arg0.equals("/ebutcher") || arg0.equals("/mobkill") || arg0.equals("/emobkill")) {
            e.setCancelled(true);
            if (args.length == 1) {
                printHelpMessage(p);
                return;
            }

            int radius = -1;
            RemoveType removeType;
            try {
                removeType = RemoveType.valueOf(args[1].toUpperCase());
                if (args.length >= 3) radius = Integer.parseInt(args[2]);
            } catch (Exception ignored) {
                printHelpMessage(p);
                return;
            }

            int removed = removeEntities(p.getWorld(), p, removeType, radius);
            p.sendMessage("§c§lKILLALL: " + "§f엔티티 §e" + removed + "§f마리를 제거하였습니다.");
        }
    }
    private void printHelpMessage(Player p) { p.sendMessage("§c§lKILLALL: §f/killall <all|named|drops|xp|mobs> [radius]"); }


    private int removeEntities(World world, Player p, RemoveType removeType, int radius) {
        int removed = 0;
        for (Entity entity : world.getEntities()) {
            if (radius > 0) {
                if (p.getPlayer().getLocation().distance(entity.getLocation()) > radius) continue;
            }
            if (entity instanceof HumanEntity || entity instanceof ArmorStand || entity instanceof ItemFrame || entity instanceof Painting) continue;

            switch (removeType) {
                case NAMED:
                    if (entity instanceof LivingEntity && entity.getCustomName() != null) {
                        entity.remove();
                        removed++;
                    }
                    break;
                case DROPS:
                    if (entity instanceof Item) {
                        entity.remove();
                        removed++;
                    }
                    break;
                case XP:
                    if (entity instanceof ExperienceOrb) {
                        entity.remove();
                        removed++;
                    }
                    break;
                case MOBS:
                    if (entity instanceof Animals || entity instanceof NPC || entity instanceof Snowman || entity instanceof WaterMob
                            || entity instanceof Monster || entity instanceof ComplexLivingEntity || entity instanceof Flying || entity instanceof Slime || entity instanceof Ambient) {
                        entity.remove();
                        removed++;
                    }
                    break;
                case ALL:
                    entity.remove();
                    removed++;
                    break;
            }
        }
        return removed;
    }

    private enum RemoveType {
        DROPS,
        XP,
        MOBS,
        ALL,
        NAMED
    }
}
