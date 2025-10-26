package cx.bird.tbt.fragmentbundle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
//    fun add(view: View) {//パターン1
//        val testFragment = Test()
//        val bundle = Bundle()
//        var atai = (0..100).random().toString()
//        bundle.putString("Key", atai)
//        testFragment.arguments = bundle
//
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.test_container, testFragment)
//        fragmentTransaction.commit()
//    }

    fun add(view: View) {//パターン2
        var atai = (0..100).random().toString()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.test_container,Test.createInstance(atai))
        fragmentTransaction.commit()
    }
}