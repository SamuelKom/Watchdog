package at.ac.fhcampuswien.watchdog.database

import androidx.room.*
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun add(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("select * from user;")
    fun getUsers(): Flow<List<User>>

    @Query("select * from user where id=:id;")
    fun getUserById(id: String): Flow<List<User>>
}