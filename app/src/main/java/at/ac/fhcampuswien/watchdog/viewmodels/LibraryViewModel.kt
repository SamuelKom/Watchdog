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
import kotlinx.coroutines.launch

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private var _currentWatchables = mutableStateListOf<Watchable>()
    val currentWatchables: List<Watchable> get() = _currentWatchables

    private val _currentList: MutableState<String> =
        mutableStateOf(value = Screen.Favorites.title)
    val currentList: State<String> = _currentList

    private val _favoriteWatchables = MutableStateFlow(listOf<LibraryItem>())
    private val _favorites = mutableStateListOf<Watchable>()

    private val _completeWatchables = MutableStateFlow(listOf<LibraryItem>())
    private val _completed = mutableStateListOf<Watchable>()

    private val _plannedWatchables = MutableStateFlow(listOf<LibraryItem>())
    private val _planned = mutableStateListOf<Watchable>()


    init {
        viewModelScope.launch {
            fetchWatchablesByLibraryItems(
                libraryItems = listOf(
                    LibraryItem(
                        TMDbID = 385687,
                        isMovie = true,
                        isFavorite = true
                    ),
                    LibraryItem(
                        TMDbID = 697843,
                        isMovie = true,
                        isPlanned = true,
                        isFavorite = true
                    ),
                    LibraryItem(
                        TMDbID = 94722,
                        isMovie = false,
                        isWatched = true,
                        isFavorite = true
                    )
                ),
                watchables = _favorites
            )
            _currentWatchables = _favorites
            //repository.getFavorites().collect() { favoriteList ->
            //    // List of library items
            //    // -> for each Fetch by id (parse favourite list as parameter)
            //    fetchWatchablesByLibraryItems(libraryItems = favoriteList, watchables = _favorites)
            //    _favoriteWatchables.value = favoriteList
            //    _currentWatchables = _favorites
            //}
            //repository.getCompleted().collect() { completeList ->
            //    _completeWatchables.value = completeList
            //    _currentWatchables = _completed
            //}
            //repository.getPlanned().collect() { plannedList ->
            //    _plannedWatchables.value = plannedList
            //    _currentWatchables = _planned
            //}
        }
    }

    // get different list functions
    fun changeList(type: Int){
        when (type){
            0 -> {
                _currentList.value = Screen.Favorites.title
                _currentWatchables = _favorites
            }
            1 -> {
                _currentList.value = Screen.Watched.title
                _currentWatchables = _completed
            }
            2 -> {
                _currentList.value = Screen.Planned.title
                _currentWatchables = _planned
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