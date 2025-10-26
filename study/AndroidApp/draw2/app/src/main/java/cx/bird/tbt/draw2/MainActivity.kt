package cx.bird.tbt.draw2


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import java.lang.Thread.sleep
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    var x = mutableListOf<Float>(1f)//X軸データ
    var atai: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    fun serial() {
        for(i in 0..50) {
            sleep(50)
            ind()
        }
    }

    fun ind() {
        var chart: BarChart = findViewById(R.id.chart1)
        //表示データ取得
        var data = BarData(getBarData())
        chart.data = data
        chart.invalidate()
    }

    //棒グラフのデータを取得
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
        thread {
            serial()
        }


    }


}

