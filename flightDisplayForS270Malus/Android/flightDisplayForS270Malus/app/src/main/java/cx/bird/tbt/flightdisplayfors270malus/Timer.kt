package cx.bird.tbt.flightdisplayfors270malus

import android.graphics.Canvas
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment



class Timer : Fragment() {

    private lateinit var timer: Chronometer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_timer, container, false)
        timer = view.findViewById(R.id.timerView)
        timer.start()
        return view
    }

    fun start(){
        timer.start()
    }

    fun stop(){
        timer.stop()
    }

}

