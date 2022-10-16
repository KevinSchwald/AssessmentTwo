import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val data = readCSV(createStream("src/main/resources/gasreading.csv"))
    var exit = false

    while (!exit) {
        printHeader()
        printMenu()
        printFooter()

        try {
            when (readln().toInt()) {
                1 -> {
                    try {
                        println("Enter month in the format mm/yyyy:")
                        val inputMonth: String? = readlnOrNull()
                        println(getMonth(inputMonth, data))
                        println("if you want to get the consumption of the chosen month enter m3 or kwh")
                        val inputConsumption: String? = readlnOrNull()
                        println(calcMonth(getMonth(inputMonth, data)!!, inputConsumption!!))
                    } catch (e: Exception) {
                        print("something went wrong, please try again")
                    }
                }

                2 -> {
                    try {
                        println("Enter year in the format yyyy:")
                        val input: String? = readlnOrNull()
                        println(getYear(input, data))
                    } catch (e: Exception) {
                        print("something went wrong, please try again")
                    }
                }

                3 -> exit = true
            }
        } catch (e: Exception) {
            println("please only enter 1,2 or 3 thanks")
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

fun getMonth(input: String?, data: List<MonthData>): MonthData? {
    var result: MonthData? = null
    val temp = input!!.split("/")
    val month = Month.of(temp[0].toInt())
    val year = temp[1].toInt()
    data.forEach {
        if ((it.beginDate.month == month) && (it.beginDate.year == year)) {
            result = it
        }
    }
    return result
}

fun getYear(year: String?, data: List<MonthData>): List<MonthData> {
    val result = mutableListOf<MonthData>()
    data.forEach {
        if (it.endDate.year == year!!.toInt()) {
            result.add(it)
        }
    }
    return result
}


fun calcMonth(month: MonthData, calc: String): Int {
    var result = 0
    if (calc == "m3") {
        result = month.endIndex - month.beginIndex
    }
    if (calc == "kwh") {
        result = ((month.endIndex - month.beginIndex) * month.kpcs).toInt()
    }
    return result
}