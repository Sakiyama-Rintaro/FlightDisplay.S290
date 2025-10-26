package cx.bird.tbt.fragmentbundle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class Test: Fragment() {
    var atai = "Hello Fragment"

        companion object {//パターン2のときコメントアウト解除
        private const val KEY1 = "Key"

        fun createInstance(atai:String): Test {
            val testFrag = Test()
            val args = Bundle()
            args.putString(KEY1, atai)
            testFrag.arguments = args
            return testFrag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle = arguments
        if (bundle != null) {
            atai = arguments!!.getString("Key").toString()
        }
        //もろもろの初期化
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //view絡みの初期化
        val view:View = inflater.inflate(R.layout.fragment_test, container, false)
        val instance:TextView = view.findViewById(R.id.textView)
        instance.text = atai

        return view

    }

}