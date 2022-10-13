import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val data = readCSV(createStream("src/main/resources/gasreading.csv"))
    var exit = false

    while (exit == false) {
        printHeader()
        printMenu()
        printFooter()

        var choice: Int = readLine()!!.toInt()

        when (choice) {
            1 -> {
                try {
                    println("Enter date in the format dd/mm/yyyy:")
                    var input: String? = readLine()
                    println(getMonth(input, data))
                } catch (e: Exception) {
                    print("something went wrong, please try again")
                }
            }

            2 -> {
                try {
                    println("Enter year in the format yyyy:")
                    var input: String? = readLine()
                    println(getYear(input, data))
                } catch (e: Exception) {
                    print("something went wrong, please try again")
                }
            }

            3 -> exit = true
        }
    }
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
**Press(1) to print the consumption per month**
**Press(2) to print the consumption per year**
***************Press(3) to exit***************
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

data class MonthData(
    val beginDate: LocalDate,
    val endDate: LocalDate,
    val beginIndex: Int,
    val endIndex: Int,
    val kpcs: Double
)

fun createStream(path: String): InputStream {
    return File(path).inputStream()
}

fun readCSV(input: InputStream): List<MonthData> {
    val reader = input.bufferedReader()
    reader.readLine() // to cut the first line out
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            val (beginDate, endDate, beginIndex, endIndex, kpcs) = it.split(',', ignoreCase = false, limit = 5)
            MonthData(
                beginDate = LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                beginIndex.toInt(),
                endIndex.toInt(),
                kpcs.toDouble()
            )
        }.toList()
}

fun getMonth(month: String?, data: List<MonthData>): MonthData? {
    var result: MonthData? = null
    data.forEach {
        if (it.beginDate <= LocalDate.parse(month, DateTimeFormatter.ofPattern("dd/MM/yyyy")) &&
            it.endDate > LocalDate.parse(month, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ) {
            result = it
        }
    }
    return result
}

fun getYear(year: String?, data: List<MonthData>): List<MonthData> {
    val result: List<MonthData> = emptyList()
    data.forEach {
        if (it.endDate.year == year!!.toInt()) {
            result.plus(it)
        }
    }
    return result
}