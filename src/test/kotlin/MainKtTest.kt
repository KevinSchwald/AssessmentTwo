import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainKtTest {

    private val data = readCSV(createStream("src/test/resources/gasreading.csv"))
    private val month = getMonth("07/2019", data)
    private val year = getYear("2019", data)

    @Test
    fun pretty() {
        assertTrue(
            "Period from 2019-06-30 - 2019-07-30, beginning at 901 and ending at 906 the conversion ratio is 10.81"
                    == pretty(getMonth("07/2019", data))
        )
    }

    @Test
    fun readCSV() {
        assertTrue(data == readCSV(createStream("src/test/resources/gasreading.csv")))
        assertFalse(data == readCSV(createStream("src/test/resources/gasreadingFake.csv")))
    }

    @Test
    fun getMonth() {
        assertTrue(month == getMonth("07/2019", data))
        assertFalse(month == getMonth("08/2019", data))
    }

    @Test
    fun getYear() {
        assertTrue(year == getYear("2019", data))
        assertFalse(year == getYear("2020", data))
    }

    @Test
    fun calcMonth() {
        assertEquals(calcMonth(month!!, "m3"), 5)
        assertEquals(calcMonth(month, "kwh"), 54)
        assertNotEquals(calcMonth(month, "m3"), 6)
        assertNotEquals(calcMonth(month, "kwh"), 55)
    }

    @Test
    fun calcYear() {
        assertTrue(
            "there were 6 months in the chosen year, the total Consumption was 2356, the max consumption was 1335 in the month of DECEMBER, the min consumption was 11 in the month of AUGUST and the average consumption in the chosen year was 392kwh."
                    == calcYear(getYear("2019", data))
        )
        assertFalse(
            "there were 6 months in the chosen year, the total Consumption was 2356, the max consumption was 1335 in the month of DECEMBER, the min consumption was 11 in the month of AUGUST and the average consumption in the chosen year was 392kwh."
                    == calcYear(getYear("2020", data))
        )
    }
}