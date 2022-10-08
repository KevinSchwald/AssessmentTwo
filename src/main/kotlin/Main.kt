import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    readCSV(createStream("src/main/resources/gasreading.csv"))
    printHeader()
    printMenu()
    printFooter()
}
fun printHeader() {
    print(
        """
**********************************************     
****** Welcome to Red Rocket Gas Station******
**********************************************
"""
    )
}

fun printMenu() {
    print(
        """
**Press(1) to print the consumption per year**
**Press(2) to print the consumption per year**
"""
    )
}

fun printFooter() {
    print(
        """
********************************************** 
********************************************** 
********************************************** 
"""
    )
}

data class Data (
    val beginDate: String,
    val endDate: String,
    val beginIndex: Int,
    val endIndex: Int,
    val kpcs: Double
        )

fun createStream(path: String): InputStream {
    return File(path).inputStream()
}

fun readCSV(input: InputStream): List<Data> {
    val reader = input.bufferedReader()
    reader.readLine() // to cut the first line out
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            val(beginDate, endDate, beginIndex, endIndex, kpcs) = it.split(',', ignoreCase = false, limit = 5)
            Data(beginDate, endDate, beginIndex.toInt(), endIndex.toInt(), kpcs.toDouble())
        }.toList()
}