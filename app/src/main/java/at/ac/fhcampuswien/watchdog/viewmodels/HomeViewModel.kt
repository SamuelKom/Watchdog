package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.Movie
import at.ac.fhcampuswien.watchdog.models.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: WatchableRepository): ViewModel() {
    //private val _movieList =  MutableStateFlow(mutableListOf<Movie>())
    //val movieList: StateFlow<List<Movie>> = _movieList.asStateFlow()

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
}