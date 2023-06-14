package at.ac.fhcampuswien.watchdog.database

import androidx.room.*
import at.ac.fhcampuswien.watchdog.models.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    //CRUD
/*
    @Insert
    suspend fun add(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("Select * from movie")
    fun getAll(): Flow<List<Movie>>

    @Query("Select * from movie where isFavorite = 1")
    fun getAllFavorite(): Flow<List<Movie>>

    @Query("Select * from movie where isComplete = 1")
    fun getAllComplete(): Flow<List<Movie>>

    @Query("Select * from movie where isPlanned = 1")
    fun getAllPlanned(): Flow<List<Movie>>

    @Query("Select * from movie where UID=:id")
    fun getMovieById(id: String): Flow<Movie>

 */
}