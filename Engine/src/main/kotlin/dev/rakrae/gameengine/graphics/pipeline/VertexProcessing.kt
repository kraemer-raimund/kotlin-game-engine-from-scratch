package dev.rakrae.gameengine.graphics.pipeline

import dev.rakrae.gameengine.graphics.Triangle
import dev.rakrae.gameengine.math.Mat4x4f

class VertexProcessing {

    fun process(
        triangleWorldSpace: Triangle,
        vertexShader: VertexShader,
        projection: Mat4x4f,
        modelView: Mat4x4f
    ): Triangle {
        with(triangleWorldSpace) {
            val vertexShaderInputs = VertexShaderInputs(projection, modelView)
            val positionsClipSpace = listOf(v0, v1, v2)
                .map { vertexShader.process(it.position.toVec3f(), vertexShaderInputs) }
            return Triangle(
                v0.copy(position = positionsClipSpace[0]),
                v1.copy(position = positionsClipSpace[1]),
                v2.copy(position = positionsClipSpace[2])
            )
        }
    }
}
