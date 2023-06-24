package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WatchableRepository): ViewModel() {

    private val _searchTextState: MutableState<String> =
        mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    private val _popularM = mutableStateListOf<Movie>()
    private val _topRated = mutableStateListOf<Movie>()

    private val _topRatedS = mutableStateListOf<Series>()
    private val _airingTodayS = mutableStateListOf<Series>()

    val popularMovies: List<Movie>
        get() = _popularM

    val topRatedMovies: List<Movie>
        get() = _topRated

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
        if (_topRated.firstOrNull{ it.UID == m.UID }  == null) {
            _topRated.add(m)
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
        if (watchable is Movie) {
            repository.updateMovie(watchable)
        } else if (watchable is Series) {
            repository.updateSeries(watchable)
        }
    }
    suspend fun updateComplete(watchable: Watchable) {
        watchable.isComplete = !watchable.isComplete
        if (watchable is Movie) {
            repository.updateMovie(watchable)
        } else if (watchable is Series) {
            repository.updateSeries(watchable)
        }
    }
    suspend fun updatePlanned(watchable: Watchable) {
        watchable.isPlanned = !watchable.isPlanned
        if (watchable is Movie) {
            repository.updateMovie(watchable)
        } else if (watchable is Series) {
            repository.updateSeries(watchable)
        }
    }

    fun checkDB(watchable: Watchable): Flow<Watchable>? {
        if (watchable is Movie) {
            return repository.getMovieByTMDbID(watchable.TMDbID.toString())
        } else if (watchable is Series) {
            return repository.getSeriesByTMDbID(watchable.TMDbID.toString())
        }
        return null
    }

    fun updateSearchTextState(newValue: String){
        _searchTextState.value = newValue
    }
}