package com.spitzer.igdbgames.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.core.network.ResultData
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.retrofit.GamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
//    private val database: GameDatabase,
    private val repository: GamesRepository
) : BaseViewModel() {

    companion object {
        private const val GAME_FETCH_NUMBER = 50
    }

    // STATES observed by the UI
    private val _gamesAddedState = MutableLiveData<Event<Boolean>>()
    val gamesAddedState: LiveData<Event<Boolean>> = _gamesAddedState

    private val _gamesLoadingState = MutableLiveData<Event<Boolean>>()
    val gamesLoadingState: LiveData<Event<Boolean>> = _gamesLoadingState

    private val _lastPageReachedState = MutableLiveData<Event<Boolean>>()
    val lastPageReachedState: LiveData<Event<Boolean>> = _lastPageReachedState
    // ----------

    var lastPageReached = false
    var isLoadingGames = false
    private var actualPage = 1

    private val _addedGames =  MutableLiveData<ArrayList<Game>>()
    val addedGames: LiveData<ArrayList<Game>> = _addedGames
    private val _loadedGames =  MutableLiveData<ArrayList<Game>>()
    val loadedGames: LiveData<ArrayList<Game>> = _loadedGames

    init {
        setLoading(true)
        loadGames()
    }

    fun setLoading(isLoading: Boolean) {
        _loading.postValue(Event(isLoading))
        isLoadingGames = isLoading
    }

    fun reloadGames() {
        setLoading(true)
        actualPage = 1
        loadGames()
    }

    fun loadGames() = viewModelScope.launch {

        val result = repository.getGames(actualPage, GAME_FETCH_NUMBER)

        when (result) {
            is ResultData.Success -> {
                setLoading(false)
                if (result.data != null) {
                    _loadedGames.value = result.data!!
                    actualPage++
                    _gamesLoadingState.value = Event(true)
                } else {
                    _snackbarError.value = Event(R.string.snackbar_could_not_fetch)
                    _gamesLoadingState.value = Event(false)
                }
            }
            is ResultData.Error -> {
                setLoading(false)
                if (result.isNetworkError()) {
                    _snackbarError.value = Event(R.string.snackbar_network_error)
                } else {
                    _snackbarError.value = Event(R.string.snackbar_error)
                }
                _gamesLoadingState.value = Event(false)
            }
        }
    }

    fun loadNextPage() = viewModelScope.launch {
        isLoadingGames = true

        val result = repository.getGames(actualPage, GAME_FETCH_NUMBER)

        when (result) {
            is ResultData.Success -> {
                if (result.data != null) {
                    _addedGames.value = result.data!!
                    _loadedGames.value?.addAll(result.data!!)
                    actualPage++
                    _gamesAddedState.value = Event(true)

                } else {
                    _snackbarError.value = Event(R.string.snackbar_could_not_fetch)
                    _gamesAddedState.value = Event(false)
                }
            }
            is ResultData.Error -> {
                if (result.isNetworkError()) {
                    _snackbarError.value = Event(R.string.snackbar_network_error)
                } else {
                    _snackbarError.value = Event(R.string.snackbar_error)
                }
                _gamesAddedState.value = Event(false)
            }
        }
    }

    fun onGameClicked(gameClicked: Game) {
        val action = MainFragmentDirections
            .actionMainFragmentToGameDetailsFragment(
                gameClicked
            )
        _navigation.value = Event(NavigationCommand.To(action))
    }
}
