package ru.mirea.svinarenkomd.data.repository

import ru.mirea.svinarenkomd.data.storage.MovieStorage
import ru.mirea.svinarenkomd.data.storage.models.Movie as StorageMovie
import ru.mirea.svinarenkomd.domain.models.Movie as DomainMovie
import ru.mirea.svinarenkomd.domain.repository.MovieRepository
import java.time.LocalDate

class MovieRepositoryImpl(
    private val movieStorage: MovieStorage
) : MovieRepository {

    override fun saveMovie(movie: DomainMovie): Boolean {
        movieStorage.save(mapToStorage(movie))
        return true
    }

    override fun getMovie(): DomainMovie {
        val movie = movieStorage.get()
        return mapToDomain(movie)
    }

    private fun mapToStorage(movie: DomainMovie): StorageMovie {
        return StorageMovie(
            id = 2,
            name = movie.name,
            localDate = LocalDate.now().toString()
        )
    }

    private fun mapToDomain(movie: StorageMovie): DomainMovie {
        return DomainMovie(
            id = movie.id,
            name = movie.name
        )
    }
}
