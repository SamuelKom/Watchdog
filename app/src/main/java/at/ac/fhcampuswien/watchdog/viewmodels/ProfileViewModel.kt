package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.UserRepository
import at.ac.fhcampuswien.watchdog.models.User
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository): ViewModel() {
    private val _users = MutableStateFlow(listOf<User?>())
    val users: StateFlow<List<User>> = _users.asStateFlow() as StateFlow<List<User>>

    init {
        viewModelScope.launch {
            repository.getUsers().collect() { userList ->
                _users.value = userList
            }
        }
    }

    suspend fun addUser(u: User) {
        if (_users.value.firstOrNull{ it?.id == u.id }  == null) {
            repository.addUser(u)
        }
    }

    suspend fun updateUser(u: User) {
        if (_users.value.firstOrNull{ it?.id == u.id }  != null) {
            repository.updateUser(u)
        }
    }

    fun getUserById(id: String): User? {
        return _users.value.firstOrNull{ it?.id == id }
    }

    fun getUserIdByIdx(idx: Int): String? {
        return if (idx >= 0 && idx < _users.value.size) users.value[idx].id
        else null
    }

    fun getUser(userID: String) : User {
        return User(
            name = "Niklas",
            color = Color.Black.toArgb(),
            favGenres = "Action", theme =
            "black"
        )
    }
}