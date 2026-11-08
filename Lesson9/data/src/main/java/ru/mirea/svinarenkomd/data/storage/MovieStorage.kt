package ru.mirea.svinarenkomd.data.storage

import ru.mirea.svinarenkomd.data.storage.models.Movie

interface MovieStorage {
    fun get(): Movie
    fun save(movie: Movie): Boolean
}
