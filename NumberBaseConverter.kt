package converter

import java.math.BigDecimal
import java.math.RoundingMode

val digit = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
    "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

fun main() {
    do {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        val res = readLine()!!
        val base = res.split(" ")
        if (base.size > 1) {
            do {
                print("Enter number in base ${base[0]} to convert to base ${base[1]} (To go back type /back) ")
                val n = readLine()!!
                if (n != "/back") {
                    val dec = toDecimal(n, base[0].toInt())
                    println("Conversion result: ${fromDecimal(dec, base[1].toBigDecimal(), n.contains('.'))}\n")
                }
            } while (n != "/back")
            println()
        }
    } while (res != "/exit")
}

fun toDecimal(number: String, base: Int): BigDecimal {
    val parts = number.split('.')
    val integerPart = parts[0]
    val fractionalPart = if (parts.size > 1) parts[1] else ""
    var result = BigDecimal.ZERO

    for (i in integerPart.indices) {
        val j = integerPart.substring(integerPart.length - i - 1, integerPart.length - i)
        result += (digit.indexOf(j.toUpperCase()).toBigDecimal() * base.toBigDecimal().pow(i))
    }

    for (i in fractionalPart.indices) {
        val j = fractionalPart.substring(i, i + 1)
        result += (digit.indexOf(j.toUpperCase()).toBigDecimal()).setScale(5) / base.toBigDecimal().pow(i + 1)
    }

    return result
}

fun fromDecimal(number: BigDecimal, base: BigDecimal, decimal: Boolean): String {
    var integerPart = number.setScale(0, RoundingMode.DOWN)
    var fractionalPart = number - integerPart
    var result = ""
    var scale = 5

    while (integerPart >= base) {
        result = digit[(integerPart % base).toInt()] + result
        integerPart = integerPart.divide(base, RoundingMode.DOWN)
    }
    if (integerPart != BigDecimal.ZERO) result = "" + digit[(integerPart % base).toInt()] + result

    if (decimal) result += "."

    while (fractionalPart > BigDecimal.ZERO && scale > 0) {
        fractionalPart *= base
        val remainder = fractionalPart.toInt()
        result += digit[remainder]
        if (remainder > 0) fractionalPart -= BigDecimal(remainder)
        scale--
    }

    if (decimal) { for (i in 0 until scale) result += "0" }

    return result
}
