package ru.mirea.svinarenkomd.data.storage.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import ru.mirea.svinarenkomd.data.storage.MovieStorage
import ru.mirea.svinarenkomd.data.storage.models.Movie
import java.time.LocalDate

class SharedPrefMovieStorage(context: Context) : MovieStorage {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun get(): Movie {
        val movieName = sharedPreferences.getString(KEY, "unknown") ?: "unknown"
        val movieDate = sharedPreferences.getString(DATE_KEY, LocalDate.now().toString()) ?: LocalDate.now().toString()
        val movieId = sharedPreferences.getInt(ID_KEY, -1)
        return Movie(movieId, movieName, movieDate)
    }

    override fun save(movie: Movie): Boolean {
        sharedPreferences.edit()
            .putString(KEY, movie.name)
            .putString(DATE_KEY, LocalDate.now().toString())
            .putInt(ID_KEY, movie.id)
            .commit()
        return true
    }

    companion object {
        private const val SHARED_PREFS_NAME = "shared_prefs_name"
        private const val KEY = "movie_name"
        private const val DATE_KEY = "movie_date"
        private const val ID_KEY = "movie_id"
    }
}
