package dev.rakrae.gameengine.graphics.pipeline

import dev.rakrae.gameengine.graphics.Bitmap
import dev.rakrae.gameengine.graphics.Buffer2f
import dev.rakrae.gameengine.graphics.Material
import dev.rakrae.gameengine.graphics.Triangle
import dev.rakrae.gameengine.math.*

class Rasterizer {

    fun rasterize(
        triangle: Triangle,
        normalWorldSpace: Vec3f,
        framebuffer: Bitmap,
        zBuffer: Buffer2f,
        material: Material,
        fragmentShader: FragmentShader
    ) {
        val triangle2i = arrayOf(triangle.v0, triangle.v1, triangle.v2)
            .map { Vec2i(it.position.x.toInt(), it.position.y.toInt()) }
            .let { Triangle2i(it[0], it[1], it[2]) }
        val boundingBox = AABB2i
            .calculateBoundingBox(triangle2i)
            .clampWithin(framebuffer.imageBounds())

        // For each point within the triangle's AABB, render the point if it lies within the triangle.
        for (x in boundingBox.min.x..boundingBox.max.x) {
            for (y in boundingBox.min.y..boundingBox.max.y) {
                val barycentricCoordinates = BarycentricCoordinates.of(Vec2i(x, y), triangle2i)
                if (barycentricCoordinates.isWithinTriangle) {
                    val interpolatedDepth = interpolateDepth(triangle, barycentricCoordinates)
                    if (interpolatedDepth < zBuffer.get(x, y)) {
                        zBuffer.set(x, y, interpolatedDepth)
                        val inputFragment = InputFragment(
                            windowSpacePosition = Vec2i(x, y),
                            interpolatedNormal = interpolateNormal(triangle, barycentricCoordinates),
                            faceNormalWorldSpace = normalWorldSpace,
                            depth = interpolatedDepth,
                            material = material
                        )
                        val outputFragment = fragmentShader.process(inputFragment)
                        framebuffer.setPixel(x, y, outputFragment.fragmentColor)
                    }
                }
            }
        }
    }

    private fun interpolateDepth(
        triangle: Triangle,
        barycentricCoordinates: BarycentricCoordinates
    ): Float {
        val z1 = triangle.v0.position.toVec3f().z.ndcToDepth()
        val z2 = triangle.v1.position.toVec3f().z.ndcToDepth()
        val z3 = triangle.v2.position.toVec3f().z.ndcToDepth()
        val b = barycentricCoordinates
        val interpolatedZ = z1 * b.a1 + z2 * b.a2 + z3 * b.a3
        return interpolatedZ
    }

    /**
     * Normalized device coordinates [-1; 1] to depth value [0; 1].
     */
    private fun Float.ndcToDepth(): Float {
        return (this + 1f) * 0.5f
    }

    private fun interpolateNormal(
        triangle: Triangle,
        barycentricCoordinates: BarycentricCoordinates
    ): Vec3f {
        val n1 = triangle.v0.normal
        val n2 = triangle.v1.normal
        val n3 = triangle.v2.normal
        val b = barycentricCoordinates
        return Vec3f(
            n1.x * b.a1 + n2.x * b.a2 + n3.x * b.a3,
            n1.y * b.a1 + n2.y * b.a2 + n3.y * b.a3,
            n1.z * b.a1 + n2.z * b.a2 + n3.z * b.a3
        )
    }

    private fun Bitmap.imageBounds(): AABB2i {
        return AABB2i(
            Vec2i(0, 0),
            Vec2i(this.width - 1, this.height - 1)
        )
    }
}
