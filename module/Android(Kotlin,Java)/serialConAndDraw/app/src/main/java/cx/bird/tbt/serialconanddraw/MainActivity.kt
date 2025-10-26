package cx.bird.tbt.serialconanddraw

import android.content.Context
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    var atai: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var intent1: TextView = findViewById(R.id.msg_1)//intent
        //var intent2: TextView = findViewById(R.id.msg_1)//intent

        var chart: BarChart = findViewById(R.id.chart1)

        //表示データ取得
        var data = BarData(getBarData())
        chart.data = data

        //Y軸(左)
        val left: YAxis = chart.axisLeft
        left.axisMinimum = 0f
        left.axisMaximum = 100f
        //left.labelCount = 5
        left.setDrawTopYLabelEntry(true)
        left.setDrawGridLines(false)

        //Y軸(右)
        val right: YAxis = chart.axisRight
        right.setDrawLabels(false)
        right.setDrawGridLines(false)
        right.setDrawZeroLine(true)
        right.setDrawTopYLabelEntry(true)

        //X軸
        val bottomAxis: XAxis = chart.xAxis
        bottomAxis.position = XAxis.XAxisPosition.BOTTOM
        bottomAxis.setDrawLabels(false)
        bottomAxis.setDrawGridLines(false)
        bottomAxis.setDrawAxisLine(false)

        //グラフ上の表示
        chart.setDrawValueAboveBar(false)
        chart.description.isEnabled = false
        chart.isClickable = false

        //凡例
        chart.legend.isEnabled = false
        chart.setScaleEnabled(false)


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
                Toast.makeText(this,"ドライバが見つかりません", Toast.LENGTH_LONG).show()
                return
            }
            val driver: UsbSerialDriver = availableDrivers[0]

            val connection: UsbDeviceConnection = manager.openDevice(driver.device)
            if (connection == null) {//かんたんにできるよアラート。とりあえず無視。
                Log.i("TAG", "connection==null")
                Toast.makeText(this,"接続できませんでした", Toast.LENGTH_LONG).show()
                //manager.requestPermission(driver.device,)
                return
            }


            port = driver.ports[0]

            port.open(connection);

            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
            Toast.makeText(this,"Connected", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this,"権限が取得できていません", Toast.LENGTH_LONG).show()
            runOnUiThread {
                var intent2: TextView = findViewById(R.id.msg_1)
                intent2.text = "権限が取得できていません"

            }
        }
    }


    fun serial() {

        var response = ByteArray(32)

        var i = 0
        while (true) {
            try {
                var len = 0
                while (len == 0) {
                    sleep(500)
                    len = port.read(response, 10)
                    //Log.i("TAG", "serlen")
                }
                Log.i("TAG", "$len")
                hantei(len,response)
            }catch (e: UninitializedPropertyAccessException){
                runOnUiThread {
                    Toast.makeText(this,"接続されていません", Toast.LENGTH_LONG).show()
                }

            }

        }
    }
    private val fixedResponse = mutableListOf<Byte>()
    fun hantei(len: Int, response: ByteArray) {
        if (len == 11) {//正常時
            ind(response)
            fixedResponse.clear()//リセット。（不正データの後に正しいデータが入るとfix関数のclear()が実行されないため。
        } else {//異常時修正ロジック
            fix(len, response)

        }
    }

    fun fix(len: Int, response: ByteArray) {

        for (i in 0 until len) {
            fixedResponse += response[i]
        }
        if (fixedResponse.size == 11) {
            ind(fixedResponse.toByteArray())//mutableList<Byte>で宣言されているため、ByteArrayに統一
            fixedResponse.clear()
        }

    }
//    fun hantei(len:Int,response:ByteArray){
//        var fixedResponse= ByteArray(32)
//        if(len==11){
//            ind(response)
//        }else{
//            while(fixedResponse.size<=11){
//
//                if(len!=11){
//                    fixedResponse+=response
//                    serial()
//                }else{
//                    ind(response)
//                }
//            }
//            ind(fixedResponse)
//        }
//    }

    fun ind(response:ByteArray) {
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
                    graph(moji)
                    Log.i("TAG", "Atai: $moji")
                    runOnUiThread {
                        var intent1: TextView = findViewById(R.id.msg_1)
                        intent1.text = "$moji"
                    }
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            runOnUiThread {
                var intent2: TextView = findViewById(R.id.msg_1)
                intent2.text = "伝送エラー"
            }
        }

    }

    fun graph(moji:String){
        //表示させるデータ
        atai = moji.toFloat()
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, atai))
        val bars: MutableList<IBarDataSet> = ArrayList()
        val dataSet = BarDataSet(entries, "bar")

        //ハイライトさせない
        dataSet.isHighlightEnabled = false

        bars.add(dataSet)

        var chart: BarChart = findViewById(R.id.chart1)
        //表示データ取得
        var data = BarData(bars)
        chart.data = data
        chart.invalidate()
    }

    fun getBarData(): List<IBarDataSet>? {
        atai+= 1f
        //表示させるデータ
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, atai))
        val bars: MutableList<IBarDataSet> = ArrayList()
        val dataSet = BarDataSet(entries, "bar")

        //ハイライトさせない
        dataSet.isHighlightEnabled = false

        bars.add(dataSet)
        return bars
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

