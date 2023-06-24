package at.ac.fhcampuswien.watchdog.database

import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series

class WatchableRepository(private val watchableDao: WatchableDao) {

    //Movie db functions
    suspend fun addMovie(movie: Movie) = watchableDao.add(movie)

    suspend fun deleteMovie(movie: Movie) = watchableDao.delete(movie)

    suspend fun updateMovie(movie: Movie) = watchableDao.update(movie)


    fun getFavoriteMovies() = watchableDao.getFavorites()

    fun getCompletedMovies() = watchableDao.getCompleted()

    fun getPlannedMovies() = watchableDao.getPlanned()

    fun getMovieByTMDbID(id: String) = watchableDao.getByTMDbID(id)

    /////////////////////////////////////////////////////
    //Series db functions

    suspend fun addSeries(series: Series) = watchableDao.add(series)

    suspend fun deleteSeries(series: Series) = watchableDao.delete(series)

    suspend fun updateSeries(series: Series) = watchableDao.update(series)


    fun getFavoriteSeries() = watchableDao.getFavorites()

    fun getCompletedSeries() = watchableDao.getCompleted()

    fun getPlannedSeries() = watchableDao.getPlanned()

    fun getSeriesByTMDbID(id: String) = watchableDao.getByTMDbID(id)
}