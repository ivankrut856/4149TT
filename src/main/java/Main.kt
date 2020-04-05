import java.lang.Math.sin
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.*


data class Point(val latitude: Float, val longitude: Float)
data class Participants(val passengers: Collection<Person>, val drivers: Collection<Person>)
data class Person(val id: UUID, val finishPoint: Point)

fun main() {
    val (passengers, drivers) = readPoints()
    for (passenger in passengers) {
        val suggestedDrivers = suggestDrivers(passenger, drivers)
        println("Passenger point: ${passenger.finishPoint.latitude}, ${passenger.finishPoint.longitude}")
        for (driver in suggestedDrivers) {
            print(getWorseness(passenger, driver))
            print(" ")
            println("  ${driver.finishPoint.latitude}, ${driver.finishPoint.longitude}")
        }
    }
}

/**
 * Converts from degrees to radians.
 * @param x the value to convert to radians.
 * @return float which is the given value in radians.
 * */
fun toRadians(x: Float): Float {
    return (x * PI / 180).toFloat();
}

/**
 * Provides a measure for how worse it is to be caught by a driver with given finish point.
 * @param passenger the passenger which is the person parametrizing the measure.
 * @param driver the driver to which the measure is to be applied.
 * @return the value of the measure for given passenger and driver.
 */
fun getWorseness(passenger: Person, driver: Person): Float {
    val meanEarthRadius = 6371e3.toFloat()
    val latitudeInRadians1 = toRadians(passenger.finishPoint.latitude)
    val latitudeInRadians2 = toRadians(driver.finishPoint.latitude)
    val deltaLatitudeInRadians = toRadians(passenger.finishPoint.latitude - driver.finishPoint.latitude)
    val deltaLongitudeInRadians = toRadians(passenger.finishPoint.longitude - driver.finishPoint.longitude)

    val alpha = sin(deltaLatitudeInRadians / 2) * sin(deltaLatitudeInRadians / 2) +
            cos(latitudeInRadians1) * cos(latitudeInRadians2) * sin(deltaLongitudeInRadians / 2) * sin(deltaLongitudeInRadians / 2)
    val c = 2 * atan2(sqrt(alpha), sqrt(1 - alpha))

    val distance = meanEarthRadius * c
    return distance
}

/**
 * Provides the ordered collection of drivers accordingly to best match for the person.
 * @param passenger the person to whom the order is to be build.
 * @param drivers the unordered collection of the drivers.
 * @return Collection<Person> which is the ordered version of the collection given.
 */
fun suggestDrivers(passenger: Person, drivers: Collection<Person>): Collection<Person> {
    return drivers.sortedBy { driver -> getWorseness(passenger, driver) }
}

private fun readPoints(): Participants {
    val pathToResource = Paths.get(Point::class.java.getResource("latlons").toURI())
    val allPoints = Files.readAllLines(pathToResource).map { asPoint(it) }.shuffled()
    val passengers = allPoints.slice(0..9).map { Person(UUID.randomUUID(), it) }
    val drivers = allPoints.slice(10..19).map { Person(UUID.randomUUID(), it) }
    return Participants(passengers, drivers)
}

private fun asPoint(it: String): Point {
    val (lat, lon) = it.split(", ")
    return Point(lat.toFloat(), lon.toFloat())
}
