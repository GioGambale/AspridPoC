import java.sql.Timestamp

class Position(val lat: String, val long: String) {
}

open class Icao(val name: String, val pos: Position) {
    constructor() : this("", Position("0", "0"))

    override fun toString(): String =
        "ICAO: { name = $name, position: [ lat = ${pos.lat} - long = ${pos.long} ] }"
}

open class CriticalAssetsConfig(val timestamp: Timestamp) {
    constructor() : this(Timestamp(System.currentTimeMillis()))
}

open class CriticalAssets(val id: String, val pos: Position, val r1: Int, val r2: Int, val r3: Int) {
    constructor() : this("", Position("0", "0"), 0, 0, 0)
}

open class Exercise(val idConfig: String) {
    constructor() : this("")
}

open class Event(
    val id: String,
    val desc: String,
    val criticalAssetsId: String,
    val entityId: String,
    val type: String,
    val timestamp: Timestamp
) {
    constructor() : this("", "", "", "", "", Timestamp(System.currentTimeMillis()))
}

open class Traffic(val id: String, val creator: String, val pos: Position, val status: String, val type: String) {
    constructor() : this("", "", Position("", ""), "", "")
}