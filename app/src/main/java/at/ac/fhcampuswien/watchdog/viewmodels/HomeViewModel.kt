package at.ac.fhcampuswien.watchdog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {

    init {
        viewModelScope.launch {
            // init routine
        }
    }

    // define functions here

}