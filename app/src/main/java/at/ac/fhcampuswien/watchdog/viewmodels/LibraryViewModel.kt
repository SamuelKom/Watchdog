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
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private val _currentWatchables = mutableStateListOf<Watchable>()
    val currentWatchables = mutableStateOf(_currentWatchables)

    private val _currentList: MutableState<String> =
        mutableStateOf(value = Screen.Favorites.title)
    val currentList: State<String> = _currentList

    private val _favorites = mutableStateListOf<Watchable>()

    private val _watched = mutableStateListOf<Watchable>()

    private val _planned = mutableStateListOf<Watchable>()


    init {
        viewModelScope.launch {
            repository.getFavorites().collect { favoriteList ->
                fetchWatchablesByLibraryItems(favoriteList, _favorites)
                println("updating fav")
                updateCurrentWatchables()
            }
        }
        viewModelScope.launch {
            repository.getWatched().collect { watchedList ->
                fetchWatchablesByLibraryItems(watchedList, _watched)
            }
        }
        viewModelScope.launch {
            repository.getPlanned().collect { plannedList ->
                fetchWatchablesByLibraryItems(plannedList, _planned)
            }
        }
    }

    // get different list functions
    private fun updateCurrentWatchables() {
        _currentWatchables.clear()
        when (_currentList.value) {
            Screen.Favorites.title -> _currentWatchables.addAll(_favorites)
            Screen.Watched.title -> _currentWatchables.addAll(_watched)
            Screen.Planned.title -> _currentWatchables.addAll(_planned)
        }
    }

    fun changeList(type: Int) {
        when (type) {
            0 -> {
                _currentList.value = Screen.Favorites.title
                updateCurrentWatchables()
            }
            1 -> {
                _currentList.value = Screen.Watched.title
                updateCurrentWatchables()
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                updateCurrentWatchables()
            }
        }
    }

    fun getListSizesFWP(): Triple<Int, Int, Int> {
        return Triple(
            _favorites.size,
            _watched.size,
            _planned.size
        )
    }

    fun updateFavorite(watchable: Watchable) {

        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (repository.exists(watchable.TMDbID.toString())) {
                repository.updateLibraryItem(item)
                repository.cleanTable()
            } else {
                repository.addLibraryItem(item)
            }
        }

        if (watchable.isFavorite.value) {
            _favorites.add(watchable)
        } else {
            _favorites.remove(watchable)
        }

        updateCurrentWatchables()
    }

    fun updateWatched(watchable: Watchable) {

        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (repository.exists(watchable.TMDbID.toString())) {
                repository.updateLibraryItem(item)
                repository.cleanTable()
            } else {
                repository.addLibraryItem(item)
            }
        }

        if (watchable.isWatched.value) {
            _watched.add(watchable)
        } else {
            _watched.remove(watchable)
        }

        updateCurrentWatchables()
    }

    fun updatePlanned(watchable: Watchable) {

        val item = LibraryItem(
            TMDbID = watchable.TMDbID,
            isMovie = watchable is Movie,
            isFavorite = watchable.isFavorite.value,
            isWatched = watchable.isWatched.value,
            isPlanned = watchable.isPlanned.value
        )

        viewModelScope.launch(Dispatchers.IO) {
            if (repository.exists(watchable.TMDbID.toString())) {
                repository.updateLibraryItem(item)
                repository.cleanTable()
            } else {
                repository.addLibraryItem(item)
            }
        }

        if (watchable.isPlanned.value) {
            _planned.add(watchable)
        } else {
            _planned.remove(watchable)
        }

        updateCurrentWatchables()
    }

    fun changeTags(watchable: Watchable){
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.exists(watchable.TMDbID.toString())) {
                repository.getByID(watchable.TMDbID.toString()).collect {item ->
                    item?.let {
                        watchable.isFavorite.value = it.isFavorite
                        watchable.isWatched.value = it.isWatched
                        watchable.isPlanned.value = it.isPlanned
                    }

                }
            }
        }
    }
}