package cx.bird.tbt.mysql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    object Gis : Table("gis") {
        val id = integer("id").autoIncrement()
        val lat = double("lat")
        val lon = double("lon")
        val rpm = float("rpm")
        val airspd = float("airspd")
        override val primaryKey = PrimaryKey(id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sqlRequest() {
        Log.i("TAG", "Hi")

        Database.connect(
            "jdbc:mysql://192.168.10.150/tele2021db",
            driver = "org.h2.Driver",
            user = "server",
            password = "k4niahcg"
        )

        transaction {
            addLogger(StdOutSqlLogger)
            Gis.insert {
                it[lat] = 1.0
                it[lon] = 2.0
                it[rpm] = 2.0f
                it[airspd] = 2.0f
            }
        }
    }

    fun start(view: View) {
        Log.i("TAG", "button")
        thread {
            sqlRequest()
            Log.i("TAG", "?")
        }

    }
}

