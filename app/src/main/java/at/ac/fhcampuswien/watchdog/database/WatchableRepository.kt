package at.ac.fhcampuswien.watchdog.database

import at.ac.fhcampuswien.watchdog.models.LibraryItem

class WatchableRepository(private val libraryDao: LibraryDao) {

    //Movie db functions
    suspend fun addLibraryItem(libraryItem: LibraryItem) = libraryDao.add(libraryItem)

    suspend fun deleteLibraryItem(libraryItem: LibraryItem) = libraryDao.delete(libraryItem)

    suspend fun updateLibraryItem(libraryItem: LibraryItem) = libraryDao.update(libraryItem)


    fun getFavorites() = libraryDao.getFavorites()

    fun getWatched() = libraryDao.getWatched()

    fun getPlanned() = libraryDao.getPlanned()

    fun getAll() = libraryDao.getAll()

    fun getByID(id: String) = libraryDao.getByID(id)

    fun exists(id: String) = libraryDao.isExists(id)

    suspend fun cleanTable() = libraryDao.cleanTable()
}