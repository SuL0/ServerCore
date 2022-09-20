package kr.sul.servercore.util

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.abs

// https://www.spigotmc.org/threads/tutorial-get-the-entity-another-entity-is-looking-at.202495/
object GetAimedPlayer {
    fun get(player: Player, maxDistance: Double): Player? {
        var targetPlayer: Player? = null
        val playerPos: Location = player.eyeLocation
        val playerDir = Vector3D(playerPos.direction)
        val playerStart = Vector3D(playerPos)
        val playerEnd: Vector3D = playerStart.add(playerDir.multiply(100))
        for (p in player.world.getNearbyPlayers(player.location, maxDistance)) {
            val targetPos = Vector3D(p.location)
            val minimum: Vector3D = targetPos.add(-0.5, 0.0, -0.5)
            val maximum: Vector3D = targetPos.add(0.5, 1.67, 0.5)
            if (p !== player && hasIntersection(playerStart, playerEnd, minimum, maximum)) {
                if (targetPlayer == null || targetPlayer.location.distanceSquared(playerPos) > p.location.distanceSquared(playerPos)) {
                    // 중간에 블럭이 없는지 체크하도록 추가
                    if (!player.hasLineOfSight(p)) {
                        targetPlayer = p
                    }
                }
            }
        }
        return targetPlayer
    }

    private fun hasIntersection(p1: Vector3D, p2: Vector3D, min: Vector3D, max: Vector3D): Boolean {
        val epsilon = 0.0001
        val d: Vector3D = p2.subtract(p1).multiply(0.5)
        val e: Vector3D = max.subtract(min).multiply(0.5)
        val c: Vector3D = p1.add(d).subtract(min.add(max).multiply(0.5))
        val ad: Vector3D = d.abs()
        if (abs(c.x) > e.x + ad.x) return false
        if (abs(c.y) > e.y + ad.y) return false
        if (abs(c.z) > e.z + ad.z) return false
        if (abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon) return false
        if (abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon) return false
        return abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x + epsilon
    }




    private class Vector3D {
        val x: Double
        val y: Double
        val z: Double

        /**
         * Construct an immutable 3D vector.
         */
        constructor(x: Double, y: Double, z: Double) {
            this.x = x
            this.y = y
            this.z = z
        }

        /**
         * Construct an immutable floating point 3D vector from a location object.
         *
         * @param location
         * - the location to copy.
         */
        constructor(location: Location) : this(location.toVector())

        /**
         * Construct an immutable floating point 3D vector from a mutable Bukkit
         * vector.
         *
         * @param vector
         * - the mutable real Bukkit vector to copy.
         */
        constructor(vector: Vector?) {
            requireNotNull(vector) { "Vector cannot be NULL." }
            x = vector.getX()
            y = vector.getY()
            z = vector.getZ()
        }

        /**
         * Convert this instance to an equivalent real 3D vector.
         *
         * @return Real 3D vector.
         */
        fun toVector(): Vector {
            return Vector(x, y, z)
        }

        /**
         * Adds the current vector and a given position vector, producing a result
         * vector.
         *
         * @param other
         * - the other vector.
         * @return The new result vector.
         */
        fun add(other: Vector3D?): Vector3D {
            requireNotNull(other) { "other cannot be NULL" }
            return Vector3D(x + other.x, y + other.y, z + other.z)
        }

        /**
         * Adds the current vector and a given vector together, producing a result
         * vector.
         *
         * @param other
         * - the other vector.
         * @return The new result vector.
         */
        fun add(x: Double, y: Double, z: Double): Vector3D {
            return Vector3D(this.x + x, this.y + y, this.z + z)
        }

        /**
         * Substracts the current vector and a given vector, producing a result
         * position.
         *
         * @param other
         * - the other position.
         * @return The new result position.
         */
        fun subtract(other: Vector3D?): Vector3D {
            requireNotNull(other) { "other cannot be NULL" }
            return Vector3D(x - other.x, y - other.y, z - other.z)
        }

        /**
         * Substracts the current vector and a given vector together, producing a
         * result vector.
         *
         * @param other
         * - the other vector.
         * @return The new result vector.
         */
        fun subtract(x: Double, y: Double, z: Double): Vector3D {
            return Vector3D(this.x - x, this.y - y, this.z - z)
        }

        /**
         * Multiply each dimension in the current vector by the given factor.
         *
         * @param factor
         * - multiplier.
         * @return The new result.
         */
        fun multiply(factor: Int): Vector3D {
            return Vector3D(x * factor, y * factor, z * factor)
        }

        /**
         * Multiply each dimension in the current vector by the given factor.
         *
         * @param factor
         * - multiplier.
         * @return The new result.
         */
        fun multiply(factor: Double): Vector3D {
            return Vector3D(x * factor, y * factor, z * factor)
        }

        /**
         * Divide each dimension in the current vector by the given divisor.
         *
         * @param divisor
         * - the divisor.
         * @return The new result.
         */
        fun divide(divisor: Int): Vector3D {
            require(divisor != 0) { "Cannot divide by null." }
            return Vector3D(x / divisor, y / divisor, z / divisor)
        }

        /**
         * Divide each dimension in the current vector by the given divisor.
         *
         * @param divisor
         * - the divisor.
         * @return The new result.
         */
        fun divide(divisor: Double): Vector3D {
            require(divisor != 0.0) { "Cannot divide by null." }
            return Vector3D(x / divisor, y / divisor, z / divisor)
        }

        /**
         * Retrieve the absolute value of this vector.
         *
         * @return The new result.
         */
        fun abs(): Vector3D {
            return Vector3D(abs(x), abs(y), abs(z))
        }

        override fun toString(): String {
            return String.format("[x: %s, y: %s, z: %s]", x, y, z)
        }

        companion object {
            /**
             * Represents the null (0, 0, 0) origin.
             */
            val ORIGIN = Vector3D(0.0, 0.0, 0.0)
        }
    }
}