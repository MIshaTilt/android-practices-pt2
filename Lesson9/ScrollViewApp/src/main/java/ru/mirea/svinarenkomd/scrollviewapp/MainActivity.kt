package ru.mirea.svinarenkomd.scrollviewapp

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wrapper = findViewById<LinearLayout>(R.id.wrapper)
        val base = BigInteger.valueOf(2)

        for (i in 0 until 100) {
            val view = layoutInflater.inflate(R.layout.item, wrapper, false)
            val text = view.findViewById<TextView>(R.id.textView)
            val value = base.pow(i)
            text.text = String.format("Элемент #%d (2^%d): %s", i + 1, i, value.toString())
            wrapper.addView(view)
        }
    }
}
