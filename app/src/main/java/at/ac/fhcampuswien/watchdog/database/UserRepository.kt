package at.ac.fhcampuswien.watchdog.database

import androidx.room.Query
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    suspend fun addUser(user: User) = userDao.add(user)

    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun updateUser(user: User) = userDao.update(user)

    fun getUsers() = userDao.getUsers()

    fun getUserById(id: String) = userDao.getUserById(id)
}
