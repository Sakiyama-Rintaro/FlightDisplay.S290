package com.example.usb

import android.content.Context
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var intent1: TextView = findViewById(R.id.msg_1)//intent
        var intent2: TextView = findViewById(R.id.msg_2)//intent


    }
    //フルスクリーン↓
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
    //フルスクリーン↑

    private lateinit var port: UsbSerialPort


    private fun serialCon() {
        try {

            Log.i("TAG", "Connected")
            var manager = getSystemService(Context.USB_SERVICE) as UsbManager
            var availableDrivers: List<UsbSerialDriver> =
                UsbSerialProber.getDefaultProber().findAllDrivers(manager)

            if (availableDrivers.isEmpty()) {
                return
            }
            val driver: UsbSerialDriver = availableDrivers[0]


            val connection: UsbDeviceConnection = manager.openDevice(driver.device)
            if (connection == null) {//かんたんにできるよアラート。とりあえず無視。
                Log.i("TAG", "connection==null")
                //manager.requestPermission(driver.device,)
                return
            }


            port = driver.ports[0]

            port.open(connection);

            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
        } catch (e: Exception) {
            runOnUiThread {
                var intent2: TextView = findViewById(R.id.msg_2)
                intent2.text = "権限が取得できていません"

            }
        }
    }

    var response = ByteArray(32)
    fun serial() {
        var i = 0
        while (true) {
            var len = 0
            while (len == 0) {
                len = port.read(response, 3)
                //Log.i("TAG", "serlen")
                sleep(100)
            }
            Log.i("TAG", "$len")
            if (len == 11) {
                ind()
            }
        }
    }

    fun ind() {
        var txCkSum: String
        var rxCkSum: Int
        try {
            Log.i(
                "TAG",
                "byte: ${response[0]},${response[1]},${response[2]},${response[3]},${response[4]},${response[5]},${response[6]},${response[7]}"
            )
            if (response[0].toChar() == 'A') {
                txCkSum = response[6].toString() + response[7].toString()
                when (txCkSum) {
                    "00" -> txCkSum = "0"
                    "01" -> txCkSum = "1"
                    "02" -> txCkSum = "2"
                    "03" -> txCkSum = "3"
                    "04" -> txCkSum = "4"
                    "05" -> txCkSum = "5"
                    "06" -> txCkSum = "6"
                    "07" -> txCkSum = "7"
                    "08" -> txCkSum = "8"
                    "09" -> txCkSum = "9"
                }
                rxCkSum = response[1] + response[2] + response[3] + response[4] + response[5]
                if (txCkSum == rxCkSum.toString()) {
                    var tmp: String = response.joinToString("")
                    var moji: String = tmp.substring(2, 7)
                    Log.i("TAG", "Atai: $moji")
                    runOnUiThread {
                        var intent1: TextView = findViewById(R.id.msg_1)
                        intent1.text = "$moji"
                    }
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            runOnUiThread {
                var intent2: TextView = findViewById(R.id.msg_2)
                intent2.text = "伝送エラー"
            }
        }
    }

    fun cone(view: View) {
        Log.i("TAG", "cone")
        serialCon()
    }

    fun read(view: View) {
        Log.i("TAG", "read")
        thread {
            serial()
        }
    }

    fun close(view: View) {
        Log.i("TAG", "close")
        port.close()
    }
}

