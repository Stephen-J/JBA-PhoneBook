fun main() {
    val input = readLine()!!.toDouble()
    val s = input.toString()
    val i = s.indexOfFirst {it == '.'}
    println(if (i != -1) {
        s[i + 1].toString().toInt()
    }else println("0"))
}