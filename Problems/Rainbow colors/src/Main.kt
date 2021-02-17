enum class Rainbow(val colorName: String) {
    RED("red"),
    ORANGE("orange"),
    YELLOW("yellow"),
    GREEN("green"),
    BLUE("blue"),
    INDIGO("indigo"),
    VIOLET("violet");

    companion object {
        fun isARainbowColor(color: String): Boolean {
            return values().firstOrNull { it.colorName == color.toLowerCase() } != null
        }
    }
}

fun main() {
    val input = readLine() ?: return
    println(Rainbow.isARainbowColor(input))
}