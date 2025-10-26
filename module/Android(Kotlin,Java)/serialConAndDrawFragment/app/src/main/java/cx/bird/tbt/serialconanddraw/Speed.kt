package cx.bird.tbt.serialconanddraw

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.service.autofill.Dataset
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class Speed : Fragment() {
    var atai:Float = 0f
    private lateinit var chart: BarChart
    val KEY1 = "speedKey"
    companion object {
        private const val KEY1 = "speedKey"

        fun createInstance(atai:Float): Speed {
            val speedFrag = Speed()
            val args = Bundle()
            args.putFloat(KEY1, atai)
            speedFrag.arguments = args
            return speedFrag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bundle = arguments
        if (bundle != null) {
            atai = arguments!!.getFloat(KEY1)
            getBarData(atai)
            Log.d("onCreate", atai.toString())
        }

    }


    lateinit var data:BarData
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Tag", "View")

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_spd, container, false)
        chart = view.findViewById(R.id.spdView)
        //表示データ取得
        data = BarData(getBarData(atai))
        chart.data = data

        //Y軸(左)
        val left: YAxis = chart.axisLeft
        left.axisMinimum = 0f
        left.axisMaximum = 12f
        left.labelCount = 5
        left.setDrawTopYLabelEntry(true)
        left.setDrawGridLines(true)

        //Y軸(右)
        val right: YAxis = chart.axisRight
        right.setDrawLabels(false)
        right.setDrawGridLines(false)
        right.setDrawZeroLine(false)
        right.setDrawTopYLabelEntry(false)

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
        data.setValueTextSize(20f)


        //凡例
        chart.legend.isEnabled = false
        chart.setScaleEnabled(false)
        return view
    }

//データ更新メソッド有効化時コメントアウト解除
//    lateinit var entries: ArrayList<BarEntry>
//    lateinit var bars: MutableList<IBarDataSet>
//    lateinit var dataSet: BarDataSet
    fun getBarData(atai:Float): List<IBarDataSet>? {
        //表示させるデータ
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(0f, atai))
        val bars: MutableList<IBarDataSet> = ArrayList()
        val dataSet = BarDataSet(entries, "bar")

        //ハイライトさせない
        dataSet.isHighlightEnabled = false
        dataSet.color = Color.parseColor("#00AA90")
        bars.add(dataSet)
        return bars
    }

//    fun graphUpdate(atai:Float){//データ更新メソッド。今回はすべて再描写させてる。
//        //表示させるデータ
//        //val entries: ArrayList<BarEntry> = ArrayList()
//        entries.add(BarEntry(0f, atai))
//        bars = ArrayList()
//        dataSet = BarDataSet(entries, "bar")
//
//        //ハイライトさせない
//        dataSet.isHighlightEnabled = false
//
//        bars.add(dataSet)
//       //表示データ取得
//        data = BarData(bars)
//        chart.data = data
//        chart.invalidate()
//    }

}


