package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.fhcampuswien.watchdog.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {
    //private val _movieList =  MutableStateFlow(mutableListOf<Movie>())
    private val _movieList = mutableStateListOf<Movie>()

    //val movieList: StateFlow<List<Movie>> = _movieList.asStateFlow()
    val movieList: List<Movie>
        get() = _movieList

    init {
        viewModelScope.launch(Dispatchers.IO) {
            coroutineScope {
            }
        }
    }

    fun addMovie(m: Movie): Boolean {
        if (movieList.firstOrNull{ it.UID == m.UID }  == null) {
            _movieList.add(m)
            println(m.title)
            return true
        }
        return false
    }
}