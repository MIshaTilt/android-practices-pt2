package ru.mirea.svinarenkomd.movieproject.data.repository

import android.content.Context
import android.content.SharedPreferences
import ru.mirea.svinarenkomd.movieproject.domain.models.Movie
import ru.mirea.svinarenkomd.movieproject.domain.repository.MovieRepository

class MovieRepositoryImpl(context: Context) : MovieRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveMovie(movie: Movie): Boolean {
        sharedPreferences.edit()
            .putInt(KEY_MOVIE_ID, movie.id)
            .putString(KEY_MOVIE_NAME, movie.name)
            .apply()
        return true
    }

    override fun getMovie(): Movie {
        val id = sharedPreferences.getInt(KEY_MOVIE_ID, 1)
        val movieName = sharedPreferences.getString(KEY_MOVIE_NAME, "Doctor Strange") ?: "Doctor Strange"
        return Movie(id, movieName)
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs_name"
        private const val KEY_MOVIE_ID = "movie_id"
        private const val KEY_MOVIE_NAME = "movie_name"
    }
}
