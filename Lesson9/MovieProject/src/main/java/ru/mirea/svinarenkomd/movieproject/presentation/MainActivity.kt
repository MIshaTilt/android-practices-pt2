package ru.mirea.svinarenkomd.movieproject.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.domain.models.Movie
import ru.mirea.svinarenkomd.movieproject.R

class MainActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(MainActivity::class.java.simpleName, "MainActivity created")

        vm = ViewModelProvider(this, ViewModelFactory(this)).get(MainViewModel::class.java)

        val text = findViewById<EditText>(R.id.editTextMovie)
        val textView = findViewById<TextView>(R.id.textViewMovie)

        vm.getFavoriteMovie().observe(this, Observer { s ->
            textView.text = s
        })

        findViewById<Button>(R.id.buttonSaveMovie).setOnClickListener {
            vm.setText(Movie(2, text.text.toString()))
        }

        findViewById<Button>(R.id.buttonGetMovie).setOnClickListener {
            vm.getText()
        }
    }
}
