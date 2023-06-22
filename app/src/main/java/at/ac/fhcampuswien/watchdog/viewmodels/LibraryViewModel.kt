package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.lifecycle.ViewModel
import at.ac.fhcampuswien.watchdog.database.WatchableRepository
import at.ac.fhcampuswien.watchdog.models.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LibraryViewModel(private val repository: WatchableRepository): ViewModel() {
    private val _currentMovies = MutableStateFlow(listOf<Movie>())
    val currentMoviesState: StateFlow<List<Movie>> = _currentMovies.asStateFlow()

    private val _favoriteMovies = MutableStateFlow(listOf<Movie>())
    private val _completeMovies = MutableStateFlow(listOf<Movie>())
    private val _plannedMovies = MutableStateFlow(listOf<Movie>())

    init {
        viewModelScope.launch {
            repository.getAllFavorite().collect() { favoriteList ->
                _favoriteMovies.value = favoriteList
                _currentMovies.value = favoriteList
            }
            repository.getAllComplete().collect() { completeList ->
                _completeMovies.value = completeList
            }
            repository.getAllPlanned().collect() { plannedList ->
                _plannedMovies.value = plannedList
            }
        }
    }

    suspend fun updateFav(movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        repository.update(movie)
    }

    // get different list functions
    fun changeList(type: Int){
        when (type){
            0 -> {
                _currentMovies.value = _favoriteMovies.value
            }
            1 -> {
                _currentMovies.value = _completeMovies.value
            }
            2 -> {
                _currentMovies.value = _plannedMovies.value
            }
        }
    }
}