package dev.rakrae.gameengine.graphics

class Material(
    val color: Color = Color(255u, 255u, 255u, 255u),
    val glossiness: Float = 0f
) {

    companion object {
        val default = Material(
            Color(255u, 255u, 255u, 255u),
            glossiness = 0f
        )
    }
}
