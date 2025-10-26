package cx.bird.tbt.fragmenttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class Test: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        instance.text = "Hello Fragment"

        return view

    }
}