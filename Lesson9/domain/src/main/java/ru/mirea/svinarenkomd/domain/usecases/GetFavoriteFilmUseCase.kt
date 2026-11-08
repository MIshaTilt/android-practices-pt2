package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.models.Movie
import ru.mirea.svinarenkomd.domain.repository.MovieRepository

class GetFavoriteFilmUseCase(
    private val movieRepository: MovieRepository
) {
    fun execute(): Movie {
        return movieRepository.getMovie()
    }
}
