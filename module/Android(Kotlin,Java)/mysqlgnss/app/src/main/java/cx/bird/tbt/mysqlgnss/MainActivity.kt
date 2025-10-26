package cx.bird.tbt.mysqlgnss

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import android.provider.Settings

class MainActivity : AppCompatActivity(),LocationListener {
    private lateinit var locationManager: LocationManager
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1000)
            } else {
                locationStart()

                if (::locationManager.isInitialized) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1f,
                        this)
                }

            }
    }

    private fun locationStart() {
        Log.d("debug", "locationStart()")

        // Instances of LocationManager class must be obtained using Context.getSystemService(Class)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // to prompt setting up GPS
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)

            Log.d("debug", "checkSelfPermission false")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            50f,
            this)

    }

    object Gis : Table("gis") {
        val db_id = integer("id").autoIncrement()
        val db_lat = double("lat")
        val db_lon = double("lon")
        val db_rpm = float("rpm")
        val db_airspd = float("airspd")
        override val primaryKey = PrimaryKey(db_id)
    }

    override fun onLocationChanged(location: Location) {

            Log.i("Tag", "changed")
            // Latitude
            var textView1 = findViewById<TextView>(R.id.text_view1)
            var lat: Double = location.latitude
            runOnUiThread(){textView1.text = lat.toString()}


            // Longitude
            var textView2 = findViewById<TextView>(R.id.text_view2)

            var lon: Double = location.longitude
            runOnUiThread(){textView2.text = lon.toString()}
            thread {
                Log.i("Tag", "tstart")
                gnssRequest(lat, lon)
                Log.i("Tag", "requestend")
            }
            Log.i("Tag", "sadonaru")

    }

    fun gnssRequest(lat:Double,lon:Double) {
        val rpm = 2.5.toFloat()
        val airspd = 3.0.toFloat()
        sqlRequest(lat,lon,rpm,airspd)
    }

    fun sqlRequest(lat: Double,lon: Double,rpm: Float,airspd: Float) {
        Log.i("TAG", "Hi")

        Database.connect(
                "jdbc:mysql://***.***.***.**:*****/tele2021db",//書き換えてください
                driver = "org.h2.Driver",
                user = "server",//書き換えてください
                password = "******"//書き換えてください
        )

        transaction {
            addLogger(StdOutSqlLogger)
            Gis.insert {
                it[db_lat] = lat
                it[db_lon] = lon
                it[db_rpm] = rpm
                it[db_airspd] = airspd
            }
        }
    }

    fun start(view: View) {
        Log.i("TAG", "button")
        thread {

            Log.i("TAG", "?")
        }

    }


}
