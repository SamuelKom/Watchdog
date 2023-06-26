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

    private val _favorites = mutableStateListOf<Watchable>()

    private val _watched = mutableStateListOf<Watchable>()

    private val _planned = mutableStateListOf<Watchable>()


    init {
        viewModelScope.launch {
            repository.getFavorites().collect() { favoriteList ->
                println("favourite" + favoriteList.size)
                fetchWatchablesByLibraryItems(libraryItems = favoriteList, watchables = _favorites)
                println("heree")
            }
            println("After loading favourites")
            repository.getWatched().collect() { watchedList ->
                println("watched" + watchedList.size)
                fetchWatchablesByLibraryItems(libraryItems = watchedList, watchables = _watched)
            }
            repository.getPlanned().collect() { plannedList ->
                fetchWatchablesByLibraryItems(libraryItems = plannedList, watchables = _planned)
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
                _currentWatchables = _favorites
            }
            1 -> {
                _currentList.value = Screen.Watched.title
                /*viewModelScope.launch {
                    repository.getFavorites().collect{ watchedList ->
                        fetchWatchablesByLibraryItems(libraryItems = watchedList, watchables = watched)
                    }
                }*/
                _currentWatchables = _watched
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                /*viewModelScope.launch {
                    repository.getFavorites().collect{ plannedList ->
                        fetchWatchablesByLibraryItems(libraryItems = plannedList, watchables = planned)
                    }
                }*/
                _currentWatchables = _planned
            }
        }
    }

    suspend fun updateFavorite(watchable: Watchable) {
        watchable.isFavorite.value = !watchable.isFavorite.value

        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
        )
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
                // Update data bank
                if (repository.exists(watchable.TMDbID.toString())) {
                    repository.updateLibraryItem(item)
                    repository.cleanTable()
                } else {
                    repository.addLibraryItem(item)
                }
                // Update local list
                if (watchable.isFavorite.value) {
                    _favorites.add(watchable)
                } else {
                    _favorites.remove(watchable)
                }
            }
        }
    }


    suspend fun updateComplete(watchable: Watchable) {
        watchable.isWatched.value = !watchable.isWatched.value
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
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
        watchable.isPlanned.value = !watchable.isPlanned.value
        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
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
                        watchable.isFavorite.value = it.isFavorite
                        watchable.isWatched.value = it.isWatched
                        watchable.isPlanned.value = it.isPlanned
                    }

                }
            }
        }
    }
}