package at.ac.fhcampuswien.watchdog.database

import at.ac.fhcampuswien.watchdog.models.LibraryItem
import at.ac.fhcampuswien.watchdog.models.Watchable

class WatchableRepository(private val watchableDao: WatchableDao) {

    //Movie db functions
    suspend fun addWatchable(watchable: Watchable) = watchableDao.add(watchable)

    suspend fun deleteWatchable(watchable: Watchable) = watchableDao.delete(watchable)

    suspend fun updateWatchable(watchable: Watchable) = watchableDao.update(watchable)


    fun getFavorites() = watchableDao.getFavorites()

    fun getCompleted() = watchableDao.getCompleted()

    fun getPlanned() = watchableDao.getPlanned()

    fun getByTMDbID(id: String) = watchableDao.getByTMDbID(id)

    fun exists(id: String) = watchableDao.isWatchableExists(id)

}