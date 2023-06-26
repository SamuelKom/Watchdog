package at.ac.fhcampuswien.watchdog.database

import androidx.room.*
import at.ac.fhcampuswien.watchdog.models.LibraryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    //CRUD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(libraryItem: LibraryItem)

    @Update
    suspend fun update(libraryItem: LibraryItem)

    @Delete
    suspend fun delete(libraryItem: LibraryItem)

    @Query("Select * from library where isFavorite = 1")
    fun getFavorites(): Flow<List<LibraryItem>>

    @Query("Select * from library where isWatched = 1")
    fun getWatched(): Flow<List<LibraryItem>>

    @Query("Select * from library where isPlanned = 1")
    fun getPlanned(): Flow<List<LibraryItem>>

    @Query("Select * from library where TMDbID=:id")
    fun getByID(id: String): Flow<LibraryItem>

    @Query("Select count(*) from library where TMDbID=:id")
    fun isExists(id: String): Boolean

    @Query("Delete from library where isFavorite = 0 and isWatched = 0 and isPlanned = 0")
    suspend fun cleanTable(): Int

}