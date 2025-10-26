
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction



object Gis : Table("gis") {
    val id = integer("id").autoIncrement()
    val lat = double("lat")
    val lon = double("lon")
    val rpm = float("rpm")
    val airspd = float("airspd")
    override val primaryKey = PrimaryKey(id)
}

fun main() {
    Database.connect("jdbc:mysql://192.168.10.150/tele2021db", driver = "org.h2.Driver", user = "server", password = "k4niahcg")

    transaction {
        addLogger(StdOutSqlLogger)
        Gis.insert {
            it[lat] = 1.0
            it[lon] = 2.0
            it[rpm] = 2.0f
            it[airspd] = 2.0f
        }
//        val gis = Gis.selectAll().toList()
//        print(gis[0])


    }
}

