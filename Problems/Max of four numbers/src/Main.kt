import kotlin.math.max

fun main() {
    val numbers = Array(4) { readLine()!!.toInt() }
    println(max(max(numbers[0],numbers[1]),max(numbers[2],numbers[3])))
}