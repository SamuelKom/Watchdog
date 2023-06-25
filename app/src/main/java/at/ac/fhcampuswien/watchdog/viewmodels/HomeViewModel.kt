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
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WatchableRepository): ViewModel() {

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    private val _popularM = mutableStateListOf<Movie>()
    private val _topRatedM = mutableStateListOf<Movie>()

    private val _topRatedS = mutableStateListOf<Series>()
    private val _airingTodayS = mutableStateListOf<Series>()

    val popularMovies: List<Movie>
        get() = _popularM

    val topRatedMovies: List<Movie>
        get() = _topRatedM

    val topRatedSeries: List<Series>
        get() = _topRatedS

    val seriesAiringToday: List<Series>
        get() = _airingTodayS

    init {
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
            }
        }
    }

    fun addPopularMovie(m: Movie) {
        if (_popularM.firstOrNull{ it.UID == m.UID }  == null) {
            _popularM.add(m)
        }
    }

    fun addTopRatedMovie(m: Movie) {
        if (_topRatedM.firstOrNull{ it.UID == m.UID }  == null) {
            _topRatedM.add(m)
        }
    }

    fun addTopRatedSeries(s: Series) {
        if (_topRatedS.firstOrNull{ it.UID == s.UID }  == null) {
            _topRatedS.add(s)
        }
    }

    fun addSeriesAiringToday(s: Series) {
        if (_airingTodayS.firstOrNull{ it.UID == s.UID }  == null) {
            _airingTodayS.add(s)
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

    suspend fun getByTMDbID(watchable: Watchable) {
        return repository.getByTMDbID(watchable.TMDbID.toString()).collect()
    }

    fun updateSearchTextState(newValue: String){
        _searchTextState.value = newValue
    }
}