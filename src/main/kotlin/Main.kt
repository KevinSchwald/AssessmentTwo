import java.io.File
import java.io.InputStream
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// The main function in which the CLI runs until exit == true
fun main() {

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
                        println(pretty(getMonth(inputMonth, data)))
                        try {
                            println("if you want to get the consumption of the chosen month enter m3 or kwh")
                            val inputConsumption: String? = readlnOrNull()
                            if (inputConsumption == "m3" || inputConsumption == "kwh") {
                                println("${calcMonth(getMonth(inputMonth, data)!!, inputConsumption)} $inputConsumption")
                            } else {
                                println("please only enter m3 or kwh")
                            }
                        } catch (e: Exception) {
                            println("something went wrong, please try again")
                        }
                    } catch (e: Exception) {
                        print("something went wrong, please try again")
                    }
                }

                2 -> {
                    try {
                        println("Enter year in the format yyyy:")
                        val inputYear: String? = readlnOrNull()
                        println(pretty(getYear(inputYear, data)))
                        try {
                            println("if you want to get a overview of the chosen year enter y otherwise enter n")
                            val inputConsumption: String? = readlnOrNull()
                            if (inputConsumption == "y") {
                                println(calcYear(getYear(inputYear, data)))
                            } else if (inputConsumption == "n") {
                                continue
                            } else {
                                println("please only enter y or n")
                            }
                        } catch (e: Exception) {
                            println("something went wrong, please try again")
                        }
                    } catch (e: Exception) {
                        println("something went wrong, please try again")
                    }
                }

                3 -> exit = true
                else -> println("please only enter 1,2 or 3 thanks")
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

// Two functions named pretty which get a List as Input and are converting the raw List into e readable String by using a String Template
fun pretty(input: MonthData?): String {
    return "Period from ${input!!.beginDate} - ${input.endDate}, beginning at ${input.beginIndex} and ending at ${input.endIndex} the conversion ratio is ${input.kpcs}"
}

fun pretty(input: List<MonthData>): String {
    var output = ""
    input.forEach {
        output += "${pretty(it)} \n"
    }
    return output
}

// A data class named MonthData because Month is already in use in Kotlin, it stores the information of exactly one month
data class MonthData(
        val beginDate: LocalDate,
        val endDate: LocalDate,
        val beginIndex: Int,
        val endIndex: Int,
        val kpcs: Double
)

// A function named createStream which converts the csv to a InputStream so that the function readCSV can work with the data provided
fun createStream(path: String): InputStream {
    return File(path).inputStream()
}

// A function named readCSV which gets a InputStream as parameter and maps the stream to the corresponding fields in the data class MonthData
fun readCSV(input: InputStream): List<MonthData> {
    val reader = input.bufferedReader()
    reader.readLine() // to cut the first line out
    try {
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
    } catch (e: Exception) {
        return emptyList()
    } finally {
        reader.close()
    }
}

// A function named getMonth which returns a month as MonthData out of a List<MonthData>
// This function is used to display the chosen month and is used to get chosen months to calculate
fun getMonth(input: String?, data: List<MonthData>): MonthData? {
    var result: MonthData? = null
    val temp = input!!.split("/")
    val month = Month.of(temp[0].toInt())
    val year = temp[1].toInt()
    data.forEach {
        if ((it.beginDate.month == month-1) && (it.beginDate.year == year)) {
            result = it
        }
    }
    return result
}

// A function which returns a List<MonthData> out of another List<MonthData>
// This Function is used to display the chosen year and is used to get the chosen year to calculate
fun getYear(year: String?, data: List<MonthData>): List<MonthData> {
    val result = mutableListOf<MonthData>()
    data.forEach {
        if (it.endDate.year == year!!.toInt()) {
            result.add(it)
        }
    }
    return result
}

// This function gets a MonthData object and the chosen calculated as input and calculates either the m3 or kwh
fun calcMonth(month: MonthData, calc: String): Int {
    var result = 0
    if (calc == "m3") {
        result = month.endIndex - month.beginIndex
    }
    if (calc == "kwh") {
        result = ((month.endIndex - month.beginIndex) * month.kpcs).roundToInt()
    }
    return result
}

// This function gets a List<MonthData> and returns a String with every calculation done
fun calcYear(data: List<MonthData>): String {

    var total = 0
    var consumption = 0
    var max = 0
    var maxMonth = Month.APRIL
    var min = Integer.MAX_VALUE
    var minMonth = Month.APRIL

    data.forEach {
        total++
        consumption += calcMonth(it, "kwh")
        if (max < calcMonth(it, "kwh")) {
            max = calcMonth(it, "kwh")
            maxMonth = it.endDate.month
        }
        if (min > calcMonth(it, "kwh")) {
            min = calcMonth(it, "kwh")
            minMonth = it.endDate.month
        }
    }

    return "there were $total months in the chosen year, " +
            "the total Consumption was $consumption, " +
            "the max consumption was $max in the month of $maxMonth, " +
            "the min consumption was $min in the month of $minMonth " +
            "and the average consumption in the chosen year was ${consumption / total}kwh."
}