package cinema

const val FRONT_HALF_PRICE = 10
const val BACK_HALF_PRICE = 8

var purchasedTicket: Int = 0
var currentIncome: Int = 0

fun main() {
    println("\nEnter the number of rows:")
    val row = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val seat = readLine()!!.toInt()

    val cinema = Array(row) { Array(seat) { "S" } }

    var response: Int
    do {
        println("\n1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        response = readLine()!!.toInt()
        when (response) {
            1 -> printCinema(cinema)
            2 -> buyTicket(cinema, row, seat)
            3 -> printStatistics(row, seat)
        }
    } while(response != 0)
}

fun printStatistics(row: Int, seat: Int) {
    val pct = purchasedTicket.toFloat() * 100 / (row * seat)
    val rounded = String.format("%.2f", pct)
    val total: Int = if (row * seat < 60) {
        row * seat * FRONT_HALF_PRICE
    } else {
        val frontHalf = row / 2
        val backHalf = row - frontHalf
        seat * (frontHalf * FRONT_HALF_PRICE + backHalf * BACK_HALF_PRICE)
    }

    println("\nNumber of purchased tickets: $purchasedTicket")
    println("Percentage: $rounded%")
    println("Current income: $$currentIncome")
    println("Total income: $$total")
}

fun buyTicket(cinema: Array<Array<String>>, row: Int, seat: Int) {
    var nok = false
    var x: Int
    var y: Int
    do {
        nok = false
        println("\nEnter a row number:")
        y = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        x = readLine()!!.toInt()

        if (y > row || x > seat) {
            println("\nWrong input!")
            nok = true
        } else if (cinema[y - 1][x - 1] == "B") {
            println("\nThat ticket has already been purchased!")
            nok = true
        }
    } while(nok)

    print("\nTicket price: ")
    val total: Int = if (row * seat < 60) {
        FRONT_HALF_PRICE
    } else {
        val frontHalf = row / 2
        if (y <= frontHalf) FRONT_HALF_PRICE else BACK_HALF_PRICE
    }
    println("$$total")
    currentIncome += total

    cinema[y - 1][x - 1] = "B"
    purchasedTicket += 1
}

fun printCinema(cinema: Array<Array<String>>) {
    println("\nCinema:")
    print(" ")
    for (i in cinema[0].indices) {
        print(" ${i + 1}")
    }
    println()
    for (i in cinema.indices) {
        print("${i + 1} ${cinema[i].joinToString(" ")}")
        println()
    }
    println()
}
