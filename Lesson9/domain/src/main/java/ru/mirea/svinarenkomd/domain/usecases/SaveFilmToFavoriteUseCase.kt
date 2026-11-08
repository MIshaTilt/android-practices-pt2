package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.Movie
import ru.mirea.svinarenkomd.domain.repository.MovieRepository

class SaveFilmToFavoriteUseCase(
    private val movieRepository: MovieRepository
) {
    fun execute(movie: Movie): Boolean {
        if (movie.name.isEmpty()) {
            return false
        }
        return movieRepository.saveMovie(movie)
    }
}
