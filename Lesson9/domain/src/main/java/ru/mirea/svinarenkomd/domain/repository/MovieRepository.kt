package ru.mirea.svinarenkomd.domain.repository

import ru.mirea.svinarenkomd.domain.models.Movie

interface MovieRepository {
    fun saveMovie(movie: Movie): Boolean
    fun getMovie(): Movie
}
