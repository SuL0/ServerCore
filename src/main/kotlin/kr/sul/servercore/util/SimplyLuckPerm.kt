package kr.sul.servercore.util

import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object SimplyLuckPerm {
    private lateinit var luckPerms: LuckPerms
    init {
        val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)
        if (provider != null) {
            luckPerms = provider.provider
        } else {
            throw Exception("Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) 에 실패")
        }
    }
    fun addPermission(userUuid: UUID, permission: String) {
        // < 방법 1 >
        // Load, modify, then save
        luckPerms.userManager.modifyUser(userUuid) { user ->
            user.data().add(Node.builder(permission).build())
        }

        // < 방법 2 >
        // val user = luckPerms.userManager.getUser(e.whoClicked.uniqueId)!!
        // val result = user.data().add(Node.builder(permissionToAdd).build())
        // luckPerms.userManager.saveUser(user)
    }
    // TODO 이거 코드 되나?
//    fun removePermission(userUuid: UUID, permission: String) {
//        luckPerms.userManager.modifyUser(userUuid) { user ->
//            user.data().remove(Node.builder(permission).build())
//        }
//    }

    fun hasPermission(p: Player, permission: String): Boolean {
        val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(p)
        return user.cachedData.permissionData.checkPermission(permission).asBoolean()
    }
}