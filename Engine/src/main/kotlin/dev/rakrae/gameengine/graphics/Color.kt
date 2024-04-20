package dev.rakrae.gameengine.graphics

data class Color(
    val r: UByte,
    val g: UByte,
    val b: UByte,
    val a: UByte
) {

    val asIntARGB: UInt
        get() {
            return (a.toUInt() shl 24) + (r.toUInt() shl 16) + (g.toUInt() shl 8) + b.toUInt()
        }

    override fun toString(): String {
        return "Color(" +
                "r=${r.toTwoDigitHexString()}, " +
                "g=${g.toTwoDigitHexString()}, " +
                "b=${b.toTwoDigitHexString()}, " +
                "a=${a.toTwoDigitHexString()})"
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun UByte.toTwoDigitHexString(): String {
        return this
            .toHexString(HexFormat.UpperCase)
            .padStart(2, '0')
    }

    companion object {
        fun fromIntARGB(intValue: UInt): Color {
            return Color(
                a = (intValue shr 24).toUByte(),
                r = (intValue shr 16).toUByte(),
                g = (intValue shr 8).toUByte(),
                b = intValue.toUByte()
            )
        }
    }
}
