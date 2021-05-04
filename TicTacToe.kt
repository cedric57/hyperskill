package tictactoe

import java.util.*
import kotlin.math.abs
import kotlin.text.*

var cells = Array(3) { CharArray(3) {' '} }

fun main() {
    val scanner = Scanner(System.`in`)

    drawGrid()

    var x = ""
    var y = ""
    var player = 'X'
    
    do {
        do {
            println("Enter the coordinates: ")
            x = scanner.next()
            y = scanner.next()
        } while (!analyseInput(x, y, player))
        player = if(player == 'X') 'O' else 'X'
        drawGrid()
    } while (!analyseGame())
}

fun analyseInput(a: String, b: String, player: Char): Boolean {
    if (a.toIntOrNull() == null || b.toIntOrNull() == null) {
        println("You should enter numbers!")
        return false
    }
    val x = a.toInt() - 1
    val y = b.toInt() - 1

    if (x !in 0..2 || y !in 0..2) {
        println("Coordinates should be from 1 to 3!")
    } else if (cells[x][y] != ' ' && cells[x][y] != '_') {
        println("This cell is occupied! Choose another one!")
    } else {
        cells[x][y] = player
        return true
    }
    return false
}

fun drawGrid() {
    println("-".repeat(9))
    for (i in 0..2) {
        println("| ${cells[i][0]} ${cells[i][1]} ${cells[i][2]} |")
    }
    println("-".repeat(9))
}

fun analyseGame(): Boolean {
    var nb = 0
    for (array in cells) {
        for (value in array) {
            if (value == 'X' || value == 'O') {
                nb++
            }
        }
    }

    var win = ""

    for (i in 0..2) {
        // lines
        if (listOf(cells[i][1], cells[i][2]).all { it == cells[i][0] }) {
            if (cells[i][0] != ' ') win += cells[i][0]
        }
        // columns
        if (listOf(cells[1][i], cells[2][i]).all { it == cells[0][i] }) {
            if (cells[0][i] != ' ') win += cells[0][i]
        }
    }

    // diagonals
    if (listOf(cells[1][1], cells[2][2]).all { it == cells[0][0] }) {
        if (cells[0][0] != ' ') win += cells[0][0]
    }
    if (listOf(cells[1][1], cells[2][0]).all { it == cells[0][2] }) {
        if (cells[0][2] != ' ') win += cells[0][2]
    }

    if (win != "") {
        println("$win wins")
        return true
    } else if (nb == 9) {
        println("Draw")
        return true
    }
    return false
}
