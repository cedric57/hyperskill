package parking

data class Car(val plate: String, val color: String)

class Parking {
    private var place = -1
    private var spots = BooleanArray(0)
    private var cars: Array<Car?> = emptyArray()
    
    fun isFull() = place <= 0
    fun isEmpty() = spots.indexOf(false) == -1
    fun isFree(spot: Int) = spots[spot - 1]
    
    fun create(place: Int): String {
        this.place = place
        spots = BooleanArray(place) { true }
        cars = arrayOfNulls<Car>(place)
        return "Created a parking lot with $place spots."
    }
    
    fun leave(spot: Int): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isFree(spot)) {
            return "There is no car in spot $spot."
        } else {
            spots[spot - 1] = true
            cars[spot - 1] = null
            place++
            return "Spot $spot is free."
        }
    }
    
    fun park(car: Car): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isFull()) {
            return "Sorry, the parking lot is full."
        } else {
            val spot = spots.indexOf(true)
            spots[spot] = false
            cars[spot] = car
            place--
            return "${car.color} car parked in spot ${spot + 1}."
        }
    }
    
    fun status(): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isEmpty()) {
            return "Parking lot is empty."
        } else {
            var s = ""
            for (spot in 1..cars.size) {
                if (!isFree(spot)) s += "${spot} ${(cars[spot - 1])!!.plate}\n"
            }
            return s.dropLast(1)
        }
    }
    
    fun reg_by_color(color: String): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isEmpty()) {
            return "Parking lot is empty."
        } else {
            var s = ""
            for (spot in 1..cars.size) {
                if (!isFree(spot) && (cars[spot - 1])!!.color.toUpperCase() == color.toUpperCase()) s += "${(cars[spot - 1])!!.plate}, "
            }
            if (s == "") return "No cars with color $color were found."
            return s.dropLast(2)
        }
    }

    fun spot_by_color(color: String): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isEmpty()) {
            return "Parking lot is empty."
        } else {
            var s = ""
            for (spot in 1..cars.size) {
                if (!isFree(spot) && (cars[spot - 1])!!.color.toUpperCase() == color.toUpperCase()) s += "$spot, "
            }
            if (s == "") return "No cars with color $color were found."
            return s.dropLast(2)
        }
    }

    fun spot_by_reg(reg: String): String {
        if (place == -1) {
            return "Sorry, a parking lot has not been created."
        } else if (isEmpty()) {
            return "Parking lot is empty."
        } else {
            for (spot in 1..cars.size) {
                if (!isFree(spot) && (cars[spot - 1])!!.plate == reg) return spot.toString()
            }
            return "No cars with registration number $reg were found."
        }
    }
}

fun main() {
    val spots = Parking()

    while (true) {
        val line = readLine()!!.split(" ")
        
        when (line[0]) {
            "create" -> println(spots.create(line[1].toInt()))
            "park" -> println(spots.park(Car(line[1], line[2])))
            "leave" -> println(spots.leave(line[1].toInt()))
            "status" -> println(spots.status())
            "reg_by_color" -> println(spots.reg_by_color(line[1]))
            "spot_by_color" -> println(spots.spot_by_color(line[1]))
            "spot_by_reg" -> println(spots.spot_by_reg(line[1]))
            "exit" -> return
        }
    }
}
