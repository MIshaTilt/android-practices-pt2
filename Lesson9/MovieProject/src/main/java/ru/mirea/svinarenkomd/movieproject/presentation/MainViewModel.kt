package ru.mirea.svinarenkomd.movieproject.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.svinarenkomd.domain.models.Movie
import ru.mirea.svinarenkomd.domain.repository.MovieRepository
import ru.mirea.svinarenkomd.domain.usecases.GetFavoriteFilmUseCase
import ru.mirea.svinarenkomd.domain.usecases.SaveFilmToFavoriteUseCase

class MainViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val favoriteMovie = MutableLiveData<String>()

    init {
        Log.d(MainViewModel::class.java.simpleName, "MainViewModel created")
    }

    fun getFavoriteMovie(): MutableLiveData<String> {
        return favoriteMovie
    }

    fun setText(movie: Movie) {
        val result = SaveFilmToFavoriteUseCase(movieRepository).execute(movie)
        favoriteMovie.value = result.toString()
    }

    fun getText() {
        val movie = GetFavoriteFilmUseCase(movieRepository).execute()
        favoriteMovie.value = String.format("My favorite movie is %s", movie.name)
    }

    override fun onCleared() {
        Log.d(MainViewModel::class.java.simpleName, "MainViewModel cleared")
        super.onCleared()
    }
}
