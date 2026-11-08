package ru.mirea.svinarenkomd.movieproject.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.data.repository.MovieRepositoryImpl
import ru.mirea.svinarenkomd.data.storage.MovieStorage
import ru.mirea.svinarenkomd.data.storage.sharedprefs.SharedPrefMovieStorage
import ru.mirea.svinarenkomd.domain.repository.MovieRepository

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sharedPrefMovieStorage: MovieStorage = SharedPrefMovieStorage(context)
        val movieRepository: MovieRepository = MovieRepositoryImpl(sharedPrefMovieStorage)
        return MainViewModel(movieRepository) as T
    }
}
