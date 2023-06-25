package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.screens.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private val _currentWatchables = MutableStateFlow(listOf<Watchable?>())
    val currentWatchablesState: StateFlow<List<Watchable>> = _currentWatchables.asStateFlow() as StateFlow<List<Watchable>>

    private val _currentList: MutableState<String> =
        mutableStateOf(value = Screen.Favorites.title)
    val currentList: State<String> = _currentList

    private val _favoriteWatchables = MutableStateFlow(listOf<Watchable?>())
    private val _completeWatchables = MutableStateFlow(listOf<Watchable?>())
    private val _plannedWatchables = MutableStateFlow(listOf<Watchable?>())

    init {
        viewModelScope.launch {
            repository.getFavorites().collect() { favoriteList ->
                _favoriteWatchables.value = favoriteList
                _currentWatchables.value = favoriteList
            }
            repository.getCompleted().collect() { completeList ->
                _completeWatchables.value = completeList
            }
            repository.getPlanned().collect() { plannedList ->
                _plannedWatchables.value = plannedList
            }
        }
    }

    // get different list functions
    fun changeList(type: Int){
        when (type){
            0 -> {
                _currentList.value = Screen.Favorites.title
                _currentWatchables.value = _favoriteWatchables.value
            }
            1 -> {
                _currentList.value = Screen.Completed.title
                _currentWatchables.value = _completeWatchables.value
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                _currentWatchables.value = _plannedWatchables.value
            }
        }
    }

    suspend fun updateFavorite(watchable: Watchable) {
        watchable.isFavorite = !watchable.isFavorite
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if(repository.exists(watchable.TMDbID.toString())){
                    repository.updateWatchable(watchable)
                }else {
                    repository.addWatchable(watchable)
                }
            }
        }
    }
    suspend fun updateComplete(watchable: Watchable) {
        watchable.isComplete = !watchable.isComplete
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if(repository.exists(watchable.TMDbID.toString())){
                    repository.updateWatchable(watchable)
                }else {
                    repository.addWatchable(watchable)
                }
            }
        }
    }
    suspend fun updatePlanned(watchable: Watchable) {
        watchable.isPlanned = !watchable.isPlanned
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if(repository.exists(watchable.TMDbID.toString())){
                    repository.updateWatchable(watchable)
                }else {
                    repository.addWatchable(watchable)
                }
            }
        }
    }
}