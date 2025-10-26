package cx.bird.tbt.serialconanddraw

import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.util.SerialInputOutputManager
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class USBCom {
    fun test(usbSerialPort:UsbSerialPort){
        var usbIoManager = SerialInputOutputManager(usbSerialPort, Listener())

            usbIoManager.start()


    }

    private inner class Listener: SerialInputOutputManager.Listener{
        override fun onRunError(e: Exception?) {
            TODO("Not yet implemented")
        }

        override fun onNewData(data: ByteArray?) {

            Log.d("data", data.contentToString())

        }
    }


}