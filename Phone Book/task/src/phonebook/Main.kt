package phonebook

import java.io.File

fun simpleSearch(directory: List<String>,toFind: List<String>) : List<String> {
    val found = mutableSetOf<String>()
    val find = toFind.map { it.toRegex() }
    for (person in find) {
        for (entry in directory) {
            if (person.find(entry) != null) {
                found += entry
                break
            }
        }
    }
    return found.toList()
}

fun convertMilliseconds(time: Long): LongArray {
    val array = LongArray(3)
    array[0] = time / 1000 / 60
    array[1] = (time - array[0] * 1000 * 60) / 1000
    array[2] = (time - array[0] * 1000 * 60 - array[1] * 1000)
    return array
}

fun main() {
    val directoryFile = File("C:\\users\\sjohnson\\directory.txt")
    val findFile = File("C:\\users\\sjohnson\\find.txt")
    val directory = directoryFile.readLines()
    val toFind = findFile.readLines()
    println("Start Searching...")
    val start = System.currentTimeMillis()
    val result = simpleSearch(directory,toFind)
    val end = System.currentTimeMillis()
    val time = convertMilliseconds(end - start)
    println("Found ${result.size} / ${toFind.size} entries. Time taken: ${time[0]} min. ${time[1]} sec. ${time[2]} ms.")
}

