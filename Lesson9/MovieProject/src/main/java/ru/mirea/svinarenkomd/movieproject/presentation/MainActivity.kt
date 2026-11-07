package ru.mirea.svinarenkomd.movieproject.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.svinarenkomd.movieproject.R
import ru.mirea.svinarenkomd.movieproject.data.repository.MovieRepositoryImpl
import ru.mirea.svinarenkomd.movieproject.domain.models.Movie
import ru.mirea.svinarenkomd.movieproject.domain.repository.MovieRepository
import ru.mirea.svinarenkomd.movieproject.domain.usecases.GetFavoriteFilmUseCase
import ru.mirea.svinarenkomd.movieproject.domain.usecases.SaveFilmToFavoriteUseCase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text = findViewById<EditText>(R.id.editTextMovie)
        val textView = findViewById<TextView>(R.id.textViewMovie)
        val movieRepository: MovieRepository = MovieRepositoryImpl(this)

        findViewById<Button>(R.id.buttonSaveMovie).setOnClickListener {
            val result = SaveFilmToFavoriteUseCase(movieRepository).execute(
                Movie(2, text.text.toString())
            )
            textView.text = String.format("Save result %s", result)
        }

        findViewById<Button>(R.id.buttonGetMovie).setOnClickListener {
            val movie = GetFavoriteFilmUseCase(movieRepository).execute()
            textView.text = String.format("Save result %s", movie.name)
        }
    }
}
