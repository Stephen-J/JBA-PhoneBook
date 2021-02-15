import kotlin.math.sqrt

fun main() {
    val input = readLine()!!.toDouble()
    val result = sqrt(sqrt(input))
    println(result)
}