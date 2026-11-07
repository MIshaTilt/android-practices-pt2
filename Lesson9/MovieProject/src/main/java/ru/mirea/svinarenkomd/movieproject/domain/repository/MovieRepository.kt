package ru.mirea.svinarenkomd.movieproject.domain.repository

import ru.mirea.svinarenkomd.movieproject.domain.models.Movie

interface MovieRepository {
    fun saveMovie(movie: Movie): Boolean
    fun getMovie(): Movie
}
