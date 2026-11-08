package ru.mirea.svinarenkomd.resultapifragmentapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), FragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val result = bundle.getString("key")
            Log.d(BottomSheetFragment::class.java.simpleName, "I'm MainActivity $result")
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.dataFragmentContainer, DataFragment::class.java, null)
                .add(R.id.blankFragmentContainer, BlankFragment::class.java, null)
                .commit()
        }
    }

    override fun sendResult(message: String) {
        Log.d(MainActivity::class.java.simpleName, "message: $message")
        Toast.makeText(this, "MainActivity получила через интерфейс: $message", Toast.LENGTH_SHORT).show()
    }
}
