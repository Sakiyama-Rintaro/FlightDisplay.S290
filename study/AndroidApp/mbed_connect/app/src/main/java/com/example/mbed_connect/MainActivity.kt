package com.example.mbed_connect

import android.content.Context
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.sun.corba.se.impl.orbutil.concurrent.SyncUtil.acquire
import android.hardware.usb.*;
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager =
            getSystemService(Context.USB_SERVICE) as UsbManager
        val usb: UsbSerialDriver = UsbSerialProber.acquire(manager)
        if (usb != null) {
            try {
                usb.open()
                usb.setBaudRate(9600)
                start_read_thread() // シリアル通信を読むスレッドを起動
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    fun start_read_thread() {
        Thread(Runnable {
            try {
                while (true) {
                    val buf = ByteArray(256)
                    val num: Int = usb.read(buf, buf.size)
                    if (num > 0) Log.v(
                        "arduino",
                        String(buf, 0, num)
                    ) // Arduinoから受信した値をlogcat出力
                    Thread.sleep(10)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }).start()
    }
}
