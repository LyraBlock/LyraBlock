package app.lyrablock.lyra.util.math

import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.joml.*

val Vector3dc.x get() = x()
val Vector3dc.y get() = y()
val Vector3dc.z get() = z()
val Vector2dc.x get() = x()
val Vector2dc.y get() = y()
val Vector2ic.x get() = x()
val Vector2ic.y get() = y()
fun Vector3dc.takeXZ(): Vector2dc = Vector2d(this.x, this.z)
fun Vector2dc.toVector2ic(): Vector2ic = Vector2i(this.x.toInt(), this.y.toInt())
fun Vec3.toVector3dc() = Vector3d(x, y, z)

operator fun Vec2.component1() = x
operator fun Vec2.component2() = y
