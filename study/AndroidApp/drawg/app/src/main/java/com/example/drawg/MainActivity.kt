package com.example.drawg

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 4f, 6f, 7f, 8f, 9f)//X軸データ
        val y = x.map{it*it}//Y軸データ（X軸の2乗）

        //①Entryにデータ格納
        var entryList = mutableListOf<BarEntry>()
        for(i in x.indices){
            entryList.add(
                BarEntry(x[i], y[i])
            )
        }

        //BarDataSetのリスト
        val barDataSets = mutableListOf<IBarDataSet>()
        //②DataSetにデータ格納
        val barDataSet = BarDataSet(entryList, "square")
        //③DataSetのフォーマット指定
        barDataSet.color = Color.BLUE
        //リストに格納
        barDataSets.add(barDataSet)

        //④BarDataにBarDataSet格納
        val barData = BarData(barDataSets)
        //⑤BarChartにBarData格納
        barChart = findViewById(R.id.barChartExample)
        barChart.data = barData
        //⑥Chartのフォーマット指定
        //X軸の設定
        barChart.xAxis.apply {
            isEnabled = true
            textColor = Color.BLACK
        }
        //⑦barchart更新
        barChart.invalidate()
    }
}