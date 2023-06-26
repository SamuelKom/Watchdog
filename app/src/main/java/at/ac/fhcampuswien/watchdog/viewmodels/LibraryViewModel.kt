package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.LibraryItem
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.screens.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private val _currentWatchables = MutableStateFlow(listOf<Watchable>())
    val currentWatchablesState: MutableStateFlow<List<Watchable>> = _currentWatchables

    private val _currentList: MutableState<String> =
        mutableStateOf(value = Screen.Favorites.title)
    val currentList: State<String> = _currentList

   /*
    init {
        viewModelScope.launch {
            repository.getFavorites().collect() { favoriteList ->
                _currentWatchables.value = favoriteList
            }
        }
    }
    */
    // get different list functions
    fun changeList(type: Int){
        when (type){
            0 -> {
                _currentList.value = Screen.Favorites.title
            }
            1 -> {
                _currentList.value = Screen.Watched.title
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                /*viewModelScope.launch(Dispatchers.IO) {
                    coroutineScope {
                        repository.getPlanned().collect() { plannedList ->
                            _currentWatchables.value = plannedList
                        }
                    }
                }*/
            }
        }
    }

    suspend fun updateFavorite(watchable: Watchable) {
        watchable.isFavorite = !watchable.isFavorite
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            poster = watchable.poster,
            isFavorite = watchable.isFavorite,
            isWatched = watchable.isWatched,
            isPlanned = watchable.isPlanned
        )
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if (repository.exists(watchable.TMDbID.toString())) {
                    repository.updateLibraryItem(item)
                    repository.cleanTable()
                } else {
                    repository.addLibraryItem(item)
                }
            }
        }
    }

    suspend fun updateComplete(watchable: Watchable) {
        watchable.isWatched = !watchable.isWatched
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            poster = watchable.poster,
            isFavorite = watchable.isFavorite,
            isWatched = watchable.isWatched,
            isPlanned = watchable.isPlanned
        )
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if (repository.exists(watchable.TMDbID.toString())) {
                    repository.updateLibraryItem(item)
                    repository.cleanTable()
                } else {
                    repository.addLibraryItem(item)
                }
            }
        }
    }

    suspend fun updatePlanned(watchable: Watchable) {
        watchable.isPlanned = !watchable.isPlanned
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            poster = watchable.poster,
            isFavorite = watchable.isFavorite,
            isWatched = watchable.isWatched,
            isPlanned = watchable.isPlanned
        )
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                if (repository.exists(watchable.TMDbID.toString())) {
                    repository.updateLibraryItem(item)
                    repository.cleanTable()
                } else {
                    repository.addLibraryItem(item)
                }
            }
        }
    }

    fun changeTags(watchable: Watchable){
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.exists(watchable.TMDbID.toString())) {
                repository.getByID(watchable.TMDbID.toString()).collect {item ->
                    item.let {
                        watchable.isFavorite = it.isFavorite
                        watchable.isWatched = it.isWatched
                        watchable.isPlanned = it.isPlanned
                    }

                }
            }
        }
    }
}