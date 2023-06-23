package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.MovieRepository
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import at.ac.fhcampuswien.watchdog.models.Watchable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository): ViewModel() {
    //private val _movieList =  MutableStateFlow(mutableListOf<Movie>())
    //val movieList: StateFlow<List<Movie>> = _movieList.asStateFlow()

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

    suspend fun toggleFavourite(watchable: Watchable) {
        watchable.isFavorite = !watchable.isFavorite
        //repository.update(watchable)
    }

    suspend fun togglePlanned(watchable: Watchable) {
        watchable.isPlanned = !watchable.isPlanned
        //repository.update(watchable)
    }

    suspend fun toggleWatched(watchable: Watchable) {
        println("Here")
        println("Before: " + watchable.isComplete)
        watchable.isComplete = !watchable.isComplete
        println("After: " + watchable.isComplete)
        //repository.update(watchable)
    }
}