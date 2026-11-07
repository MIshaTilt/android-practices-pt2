package ru.mirea.svinarenkomd.movieproject.domain.usecases

import ru.mirea.svinarenkomd.movieproject.domain.models.Movie
import ru.mirea.svinarenkomd.movieproject.domain.repository.MovieRepository

class GetFavoriteFilmUseCase(
    private val movieRepository: MovieRepository
) {
    fun execute(): Movie {
        return movieRepository.getMovie()
    }
}
