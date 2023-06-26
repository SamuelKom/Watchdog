package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.LibraryItem
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Watchable
import at.ac.fhcampuswien.watchdog.screens.Screen
import at.ac.fhcampuswien.watchdog.tmdb_api.fetchWatchablesByLibraryItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private var _currentWatchables = mutableStateListOf<Watchable>()
    val currentWatchables: MutableList<Watchable> get() = _currentWatchables

    private val _currentList: MutableState<String> =
        mutableStateOf(value = Screen.Favorites.title)
    val currentList: State<String> = _currentList

    private val _favoriteWatchables = MutableStateFlow(listOf<LibraryItem>())
    val favoriteWatchables: StateFlow<List<LibraryItem>> = _favoriteWatchables.asStateFlow()
    private val favorites = mutableStateListOf<Watchable>()

    private val _watchedWatchables = MutableStateFlow(listOf<LibraryItem>())
    val watchedWatchables: StateFlow<List<LibraryItem>> = _watchedWatchables.asStateFlow()
    private val watched = mutableStateListOf<Watchable>()

    private val _plannedWatchables = MutableStateFlow(listOf<LibraryItem>())
    val plannedWatchables: StateFlow<List<LibraryItem>> = _plannedWatchables.asStateFlow()
    private val planned = mutableStateListOf<Watchable>()


    init {
        viewModelScope.launch {
            repository.getFavorites().collect() { favoriteList ->
                _favoriteWatchables.value = favoriteList
                fetchWatchablesByLibraryItems(libraryItems = favoriteList, watchables = favorites)
            }
            repository.getWatched().collect() { watchedList ->
                _watchedWatchables.value = watchedList
                println("watched" + watchedList.size)
                fetchWatchablesByLibraryItems(libraryItems = watchedList, watchables = watched)
                _currentWatchables = watched
            }
            repository.getPlanned().collect() { plannedList ->
                _plannedWatchables.value = plannedList
                fetchWatchablesByLibraryItems(libraryItems = plannedList, watchables = planned)
            }
        }
    }

    // get different list functions
    fun changeList(type: Int){
        when (type){
            0 -> {
                _currentList.value = Screen.Favorites.title
                /*viewModelScope.launch {
                    repository.getFavorites().collect{ favoriteList ->
                        fetchWatchablesByLibraryItems(libraryItems = favoriteList, watchables = favorites)
                    }
                }*/
                _currentWatchables = favorites
            }
            1 -> {
                _currentList.value = Screen.Watched.title
                /*viewModelScope.launch {
                    repository.getFavorites().collect{ watchedList ->
                        fetchWatchablesByLibraryItems(libraryItems = watchedList, watchables = watched)
                    }
                }*/
                _currentWatchables = watched
                println("Length" + watched.size)
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                /*viewModelScope.launch {
                    repository.getFavorites().collect{ plannedList ->
                        fetchWatchablesByLibraryItems(libraryItems = plannedList, watchables = planned)
                    }
                }*/
                _currentWatchables = planned
            }
        }
    }

    suspend fun updateFavorite(watchable: Watchable) {
        watchable.isFavorite = !watchable.isFavorite
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
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