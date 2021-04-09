package kr.sul.servercore.nbtapi

import net.minecraft.server.v1_12_R1.*
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


// SkNbeet 에서 가져온 코드. 사실 그냥 NBT-API 써도 됨
object NbtApiFromNbeet {
    fun getCompoundClass(): Class<NBTTagCompound> {
        return NBTTagCompound::class.java
    }

    fun getNBT(b: Block): String? {
        val w = (b.world as CraftWorld).handle
        val nbt = NBTTagCompound()
        val tile: TileEntity = w.getTileEntity(BlockPosition(b.x, b.y, b.z))
            ?: return null
        tile.save(nbt)
        return nbt.toString()
    }

    fun setNBT(b: Block, value: String) {
        val w = (b.world as CraftWorld).handle
        val tile: TileEntity = w.getTileEntity(BlockPosition(b.x, b.y, b.z))
            ?: return
        try {
            val nbt = MojangsonParser.parse(value)
            tile.load(nbt)
        } catch (ex: MojangsonParseException) {
        }
        tile.update()
    }

    fun addNBT(b: Block, value: String) {
        val w = (b.world as CraftWorld).handle
        val tile = w.getTileEntity(BlockPosition(b.x, b.y, b.z))
        val nbt = NBTTagCompound()
        if (tile == null) return
        tile.save(nbt)
        try {
            val nbtv = MojangsonParser.parse(value)
            nbt.a(nbtv)
            tile.load(nbt)
        } catch (ex: MojangsonParseException) {
        }
        tile.update()
    }

    fun getNBT(e: Entity): String? {
        val nms = (e as CraftEntity).handle
        val nbt = NBTTagCompound()
        if (e is Player) {
            nms.save(nbt)
        } else {
            nms.c(nbt)
        }
        return nbt.toString()
    }

    fun addNBT(e: Entity, value: String) {
        val nms = (e as CraftEntity).handle
        val nbt = NBTTagCompound()
        if (e is Player) {
            nms.save(nbt)
        } else {
            nms.c(nbt)
        }
        try {
            val nbtv = MojangsonParser.parse(value)
            nbt.a(nbtv)
            nms.f(nbt)
        } catch (ex: MojangsonParseException) {
        }
    }

    fun setNBT(e: Entity, value: String) {
        val nms = (e as CraftEntity).handle
        try {
            val nbtv = MojangsonParser.parse(value)
            nms.f(nbtv)
        } catch (ex: MojangsonParseException) {
        }
    }


    fun getNBT(i: ItemStack?): String? {
        if (i == null || i.type == Material.AIR) return null
        val nbt = CraftItemStack.asNMSCopy(i).tag ?: return null
        return nbt.toString()
    }

    fun addNBT(i: ItemStack, value: String) {
        val nms = CraftItemStack.asNMSCopy(i)
        var nbt = NBTTagCompound()
        if (nms.tag != null) {
            nbt = nms.tag!!
        }
        try {
            val nbtv = MojangsonParser.parse(value)
            nbt.a(nbtv)
            nms.tag = nbt
        } catch (ex: MojangsonParseException) {
        }
        i.itemMeta = CraftItemStack.asBukkitCopy(nms).itemMeta
    }

    fun getNBTTag(a: String?, b: String?): Array<String>? {
        return if (a == null || b == null) {
            null
        } else try {
            val nbt = MojangsonParser.parse(b)
            if (nbt[a] != null) {
                arrayOf(nbt[a].toString())
            } else {
                null
            }
        } catch (ex: MojangsonParseException) {
            null
        }
    }

    fun getEntityNoClip(e: Entity): Boolean {
        val entity = (e as CraftEntity).handle
        return entity.noclip
    }

    fun setEntityNoClip(e: Entity, value: Boolean?) {
        val entity = (e as CraftEntity).handle
        entity.noclip = value!!
    }

    fun getJoinedNBTList(list: Array<String?>): Array<String>? {
        return try {
            val nbto = NBTTagCompound()
            var nbtj: NBTTagCompound?
            for (nbt in list) {
                nbtj = MojangsonParser.parse(nbt)
                nbto.a(nbtj)
            }
            arrayOf(
                nbto.toString()
            )
        } catch (ex: MojangsonParseException) {
            null
        }
    }
}