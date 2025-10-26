package com.example.vsantoni


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.EditText
const val EXTRA_MESSAGE = "com.example.VSantoni.MESSAGE"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.player_name)
        val message = editText.text.toString()
        val intent = Intent(this@MainActivity,gameActivity::class.java).apply{
                putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

}
