fun main() {
    val words = mutableMapOf<String, Int>()
    do {
        val input = readLine() ?: return
        if (input != "stop" && input.isNotBlank()) {
            words[input] = words.getOrDefault(input, 0) + 1
        }
    } while (input != "stop")
    val result: String? = if (words.isNotEmpty()) {
        words.entries.reduce { max, next -> if (next.value > max.value) next else max }.key
    } else null
    println(result)
}