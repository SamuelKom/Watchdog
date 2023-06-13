package at.ac.fhcampuswien.watchdog.database

import at.ac.fhcampuswien.watchdog.models.Movie

class MovieRepository(private val movieDao: MovieDao) {
    suspend fun add(movie: Movie) = movieDao.add(movie)

    suspend fun delete(movie: Movie) = movieDao.delete(movie)

    suspend fun update(movie: Movie) = movieDao.update(movie)

    fun getAll() = movieDao.getAll()

    fun getAllFavorite() = movieDao.getAllFavorite()

    fun getAllComplete() = movieDao.getAllComplete()

    fun getAllPlanned() = movieDao.getAllPlanned()

    fun getMovieById(id: String) = movieDao.getMovieById(id)
}