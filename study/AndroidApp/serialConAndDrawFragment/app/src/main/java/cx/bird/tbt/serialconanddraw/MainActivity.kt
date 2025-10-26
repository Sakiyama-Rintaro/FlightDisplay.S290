package cx.bird.tbt.serialconanddraw

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class MainActivity : AppCompatActivity() {


    lateinit var fragmentTransaction:FragmentTransaction
    lateinit var fragmentManager:FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.spd_container,Speed.createInstance(0f))
        fragmentTransaction.commit()


    }

    fun add(view: View) {
        var atai = (0..100).random().toFloat()/10
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.spd_container,Speed.createInstance(atai))
        fragmentTransaction.commit()
//        var fm = supportFragmentManager.findFragmentById(R.id.spd_container) as Speed //Fragment中のメソッドを呼び出すことも出来る
//        fm.graphUpdate(atai)
        Log.d("send", atai.toString())


    }
}