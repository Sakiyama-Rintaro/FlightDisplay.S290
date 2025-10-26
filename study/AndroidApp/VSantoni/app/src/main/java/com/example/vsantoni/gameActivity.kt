package com.example.vsantoni

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game.*

class gameActivity : AppCompatActivity() {
    val random = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val textView = findViewById<TextView>(R.id.player_name).apply {
            text = message
        }

    }

    fun atk(view: View) {

        var aiu = "アンコニペラペラ"

        var intent: TextView = findViewById(R.id.msg_1)
        intent.text = "魔法によって名前が${aiu}になった！"
        status(aiu)
    }

    fun def(view: View) {

        var aiu = "アンコニペラペラ"

        var intent: TextView = findViewById(R.id.msg_1)
        intent.text = "魔法によって名前が${aiu}になった！"
        status(aiu)
    }

    fun esc(view: View) {

    }

    fun status(aiu: String) {
        var name:TextView = findViewById(R.id.player_name)
        name.text = "${aiu}"
    }


}




