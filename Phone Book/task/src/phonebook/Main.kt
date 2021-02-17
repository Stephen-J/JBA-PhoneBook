package phonebook

import java.io.File
import kotlin.math.floor
import kotlin.math.sqrt

const val DIRECTORY_NAME = "C:\\users\\sjohnson\\directory.txt"
const val FIND_NAME = "C:\\users\\sjohnson\\find.txt"
const val SORTED_DIRECTORY = "C:\\users\\sjohnson\\directory_sorted.txt"

data class Record(val phoneNumber: String, val name: String) : Comparable<Record> {

    override fun toString() = "$phoneNumber $name"

    override fun compareTo(other: Record) = name.toLowerCase().compareTo(other.name.toLowerCase())

    companion object {
        fun fromString(input: String): Record {
            val index = input.indexOfFirst { it == ' ' }
            return Record(input.substring(0..index), input.substring(index + 1))
        }
    }

    object Query {
        fun nameEquals(record: Record, name: String) = record.name.equals(name, ignoreCase = true)
    }
}

fun <T, K> simpleSearch(directory: List<T>, toFind: List<K>, query: (T, K) -> Boolean): List<T> {
    val found = mutableListOf<T>()
    for (k in toFind) {
        for (t in directory) {
            if (query(t, k)) {
                found += t
                break
            }
        }
    }
    return found.toList()
}

fun <T, K> jumpSearch(list: List<T>, toFind: List<K>, getter: (T) -> K): List<T>
        where T : Comparable<T>,
              K : Comparable<K> {
    val found = mutableListOf<T>()
    val blockSize = floor(sqrt(list.size.toDouble())).toInt()
    val blocks = list.indices.chunked(blockSize)
    for (k in toFind) {
        if (getter(list.first()) > k || getter(list.last()) < k) continue
        var tmp: K
        for (block in blocks) {
            tmp = getter(list[block.last()])
            if (k <= tmp) {
                for (i in block.last() downTo block.first()) {
                    tmp = getter(list[i])
                    if (tmp == k) {
                        found += list[i]
                        break
                    }
                }
                break
            }
        }
    }
    return found
}

fun <T, K> binarySearch(list: List<T>, toFind: List<K>, getter: (T) -> K): List<T>
        where T : Comparable<T>,
              K : Comparable<K> {
    val results = mutableListOf<T>()
    for (k in toFind) {
        var low = 0
        var hi = list.size - 1
        var mid = (low + hi) / 2
        var tmp: K
        while (low < hi) {
            tmp = getter(list[mid])
            if (k < tmp) {
                hi = mid
                mid = (low + hi) / 2
            } else if (k > tmp) {
                low = mid
                mid = (low + hi) / 2
            } else if (k == tmp) {
                results += list[mid]
                break
            }
        }
    }
    return results
}

fun <T> bubbleSort(list: MutableList<T>, timeout: Long): Boolean
        where T : Comparable<T> {
    var tmp: T
    var beginLoop: Long
    var time = 0L
    var end = list.size - 1
    var timedOut = false
    var swapped = true
    while (end >= 0 && !timedOut && swapped) {
        swapped = false
        beginLoop = System.currentTimeMillis()
        for (i in 0 until end) {
            if (list[i] > list[i + 1]) {
                tmp = list[i]
                list[i] = list[i + 1]
                list[i + 1] = tmp
                swapped = true
            }
        }
        time += System.currentTimeMillis() - beginLoop
        if (time > timeout) {
            timedOut = true
            break
        }
        end--
    }
    return timedOut
}

fun <T> quickSort(list: MutableList<T>, low: Int, hi: Int)
        where T : Comparable<T> {
    if (low < hi) {
        val pivot = list[floor((hi + low) / 2.0).toInt()]
        var i = low - 1
        var j = hi + 1
        var tmp: T
        while (true) {
            do i += 1 while (list[i] < pivot)
            do j -= 1 while (list[j] > pivot)
            if (i >= j) break
            tmp = list[i]
            list[i] = list[j]
            list[j] = tmp
        }
        quickSort(list, low, j)
        quickSort(list, j + 1, hi)
    }


}

fun convertMilliseconds(time: Long): LongArray {
    val array = LongArray(3)
    array[0] = time / 1000 / 60
    array[1] = (time - array[0] * 1000 * 60) / 1000
    array[2] = (time - array[0] * 1000 * 60 - array[1] * 1000)
    return array
}

fun <T> timeIt(f: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = f()
    val end = System.currentTimeMillis()
    return result to (end - start)
}

fun buildTimeString(timing: Long): String {
    val converted = convertMilliseconds(timing)
    return "Time taken: ${converted[0]} min. ${converted[1]} sec. ${converted[2]} ms."
}

fun doSimpleSearch(directory: List<Record>, toFind: List<String>): Long {
    println("Start Searching (linear search)...")
    val (result, timing) = timeIt { simpleSearch(directory, toFind, Record.Query::nameEquals) }
    println("Found ${result.size} / ${toFind.size} entries. ${buildTimeString(timing)}")
    return timing
}

fun doBubbleSortAndJumpSearch(directory: List<Record>, toFind: List<String>, timeout: Long) {
    println("Start Searching (bubble sort + jump search)...")
    val dir = directory.toMutableList()
    val (timedOut, sortTiming) = timeIt { bubbleSort(dir, timeout) }
    if (!timedOut) {
        val (results, resultTiming) = timeIt { jumpSearch(dir, toFind) { it.name } }
        println("Found ${results.size} / ${toFind.size} entries. ${buildTimeString(resultTiming + sortTiming)}")
        println("Sorting time: ${buildTimeString(sortTiming)}")
        println("Searching time: ${buildTimeString(resultTiming)}")
        val file = File(SORTED_DIRECTORY)
        file.writeText(dir.joinToString("\n"))
    } else {
        val (results, resultTiming) = timeIt { simpleSearch(directory, toFind, Record.Query::nameEquals) }
        println("Found ${results.size} / ${toFind.size} entries. ${buildTimeString(resultTiming + sortTiming)}")
        println("Sorting time: ${buildTimeString(sortTiming)} - STOPPED, moved to linear search")
        println("Searching time: ${buildTimeString(resultTiming)}")
    }
}

fun doQuickSortAndBinarySearch(directory: List<Record>, toFind: List<String>) {
    println("Start Searching (quick sort + binary search)...")
    val dir = directory.toMutableList()
    val (_, sortTiming) = timeIt { quickSort(dir, 0, dir.size - 1) }
    val file = File(SORTED_DIRECTORY)
    if (!file.exists()) file.writeText(dir.joinToString("\n"))
    val (results, resultTiming) = timeIt {
        binarySearch(
            dir,
            toFind.map { it.toLowerCase() }) { it.name.toLowerCase() }
    }
    println("Found ${results.size} / ${toFind.size} entries. ${buildTimeString(resultTiming + sortTiming)}")
    println("Sorting time: ${buildTimeString(sortTiming)}")
    println("Searching time: ${buildTimeString(resultTiming)}")
}

fun main() {
    val directoryFile = File(DIRECTORY_NAME)
    val findFile = File(FIND_NAME)
    val directory = directoryFile.readLines().map { Record.fromString(it) }
    val toFind = findFile.readLines()
    val timeout = doSimpleSearch(directory, toFind)
    println()
    doBubbleSortAndJumpSearch(directory, toFind, timeout * 10)
    println()
    doQuickSortAndBinarySearch(directory, toFind)
}