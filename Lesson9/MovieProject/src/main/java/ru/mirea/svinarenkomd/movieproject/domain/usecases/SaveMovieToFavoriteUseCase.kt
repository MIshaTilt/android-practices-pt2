package ru.mirea.svinarenkomd.movieproject.domain.usecases

import ru.mirea.svinarenkomd.movieproject.domain.models.Movie
import ru.mirea.svinarenkomd.movieproject.domain.repository.MovieRepository

class SaveMovieToFavoriteUseCase(
    private val movieRepository: MovieRepository
) {
    fun execute(movie: Movie): Boolean {
        if (movie.name.isEmpty()) {
            return false
        }
        return movieRepository.saveMovie(movie)
    }
}
