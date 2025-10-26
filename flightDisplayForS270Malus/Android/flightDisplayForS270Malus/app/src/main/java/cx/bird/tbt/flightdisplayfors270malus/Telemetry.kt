package cx.bird.tbt.flightdisplayfors270malus

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import java.lang.Exception
import java.util.logging.Handler
import kotlin.concurrent.thread
import kotlin.math.round
import kotlin.math.roundToInt

class Telemetry(url: String,user: String,pass: String) {

    init {
        Database.connect(
            "jdbc:mysql://$url",
            driver = "org.h2.Driver",
            user = user,
            password = pass
        )
        Log.d("Tele","DBCon")

    }

    object Gis : Table("gis") {
        val db_id = integer("id").autoIncrement()
        val db_lat = double("lat")
        val db_lon = double("lon")
        val db_rpm = float("rpm")
        val db_airspd = float("airspd")
        override val primaryKey = PrimaryKey(db_id)
    }

    fun ckDbStatus(): Int?{
//        transaction {
//            addLogger(StdOutSqlLogger)
//            Gis.select { Gis.db_id }
//        }
        return 0
    }

    fun sqlRequest(newLat: Double,newLon: Double,alt: Float,airspd: Float) {
        var latValue = (newLat * 1000000.0).roundToInt() / 1000000.0
        var lonValue = (newLon * 1000000.0).roundToInt() / 1000000.0
        Log.d("LAT",latValue.toString())
        if(lonValue!=0.0 && latValue!=0.0){


                transaction {
                    addLogger(StdOutSqlLogger)
                    Gis.insert {
                        it[db_lat] = latValue
                        it[db_lon] = lonValue
                        it[db_rpm] = alt
                        it[db_airspd] = airspd
                    }
                }
        }
    }
}