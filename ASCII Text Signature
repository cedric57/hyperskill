package signature

import java.io.File

class Letter(val name: String, val wide: Int) {
    var value: Array<String>? = null
}

class Font(filename: String, space: Int) {
    var height: Int = 0
    var letters = mutableMapOf<String, Letter>()

    init {
        val lines = File(filename).readLines(Charsets.US_ASCII)
        val iter = lines.iterator()
        val par = iter.next().split(" ")
        height = par[0].toInt()

        while (iter.hasNext()) {
            val par2 = iter.next().split(" ")
            val name = par2[0]
            val letter = Letter(name, par2[1].toInt())
            val value = Array(height) { "" }
            for (j in 0 until height) {
                value[j] = iter.next()
            }
            letter.value = value
            letters[name] = letter
        }
        val name = " "
        val letter = Letter(name, space)
        letter.value = Array(height) { name.repeat(space) }
        letters[name] = letter
    }
}

class FontString(private var font: Font, value: String) {
    var wide: Int = 0
    private var letters = ArrayList<Letter>()

    init {
        for (c in value) {
            val letter = font.letters[c.toString()]!!
            letters.add(letter)
            wide += letter.wide
        }
    }

    fun toString(size: Int): String {
        var s = ""
        for (i in 0 until font.height) {
            s += "88" + " ".repeat((size - wide) / 2 - 2)
            letters.forEach {
                s += it.value!![i]
            }
            s += " ".repeat(size - 2 - (size + wide) / 2) + "88\n"
        }
        return s
    }
}

fun main() {
    val medium = Font("c:/dev/medium.txt", 5)
    val roman = Font("c:/dev/roman.txt", 10)

    print("Enter name and surname: ")
    val name = readLine()!!
    print("Enter person's status: ")
    val status = readLine()!!

    val nameLine = FontString(roman, name)
    val statusLine = FontString(medium, status)

    val wide = 8 + if (nameLine.wide >= statusLine.wide) nameLine.wide else statusLine.wide

    val star = "8".repeat(wide)

    println(star)
    print(nameLine.toString(wide))
    print(statusLine.toString(wide))
    println(star)
}
