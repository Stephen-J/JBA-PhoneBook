import java.util.Scanner
import kotlin.math.acos
import kotlin.math.hypot

fun main() {
    val scanner = Scanner(System.`in`)
    val a = Pair(scanner.nextDouble(),scanner.nextDouble())
    val b = Pair(scanner.nextDouble(),scanner.nextDouble())
    val cos = (a.first * b.first + a.second * b.second) / (hypot(a.first,a.second) * hypot(b.first,b.second))
    val degrees = Math.toDegrees(acos(cos))
    println(degrees)
}