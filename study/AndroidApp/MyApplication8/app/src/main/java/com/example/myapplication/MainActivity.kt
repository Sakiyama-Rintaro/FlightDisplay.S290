package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var a = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCount = findViewById<Button>(R.id.butCount)
        val listener = Listener()
        buttonCount.setOnClickListener(listener)
        }

    private inner class Listener : View.OnClickListener{

        override fun onClick(view: View) {

            var output = findViewById<TextView>(R.id.numCount)
            a++

                output.text = a.toString()
        }
    }
}
