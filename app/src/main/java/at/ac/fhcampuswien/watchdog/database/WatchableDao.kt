package at.ac.fhcampuswien.watchdog.database

import androidx.room.*
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchableDao {
    //CRUD

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(watchable: Watchable)

    @Update
    suspend fun update(watchable: Watchable)

    @Delete
    suspend fun delete(watchable: Watchable)

    @Query("DELETE FROM watchable")
    fun nukeTable()

    @Query("Select * from watchable where isFavorite = 1")
    fun getFavorites(): Flow<List<Watchable>>

    @Query("Select * from watchable where isComplete = 1")
    fun getCompleted(): Flow<List<Watchable>>

    @Query("Select * from watchable where isPlanned = 1")
    fun getPlanned(): Flow<List<Watchable>>

    @Query("Select * from watchable where TMDbID=:tmdbid")
    fun getByTMDbID(tmdbid: String): Flow<Watchable?>

    @Query("SELECT COUNT(*) from watchable where TMDbID=:tmdbid")
    fun isWatchableExists(tmdbid: String): Boolean

}