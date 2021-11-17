package com.spitzer.igdbgames.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.GamesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GamesRepository
) : BaseViewModel() {

    // STATES observed by the UI
    private val _gamesAddedState = MutableLiveData<Event<Boolean>>()
    val gamesAddedState: LiveData<Event<Boolean>> = _gamesAddedState

    private val _gamesLoadingState = MutableLiveData<Event<Boolean>>()
    val gamesLoadingState: LiveData<Event<Boolean>> = _gamesLoadingState

    private val _lastPageReachedState = MutableLiveData<Event<Boolean>>()
    val lastPageReachedState: LiveData<Event<Boolean>> = _lastPageReachedState
    // ----------

    var lastPageReached = false
    var isLoadingNextPage = false
    private var actualPage = 1

    private val _addedGames = MutableLiveData<ArrayList<Game>>()
    val addedGames: LiveData<ArrayList<Game>> = _addedGames
    private val _loadedGames = MutableLiveData<ArrayList<Game>>()
    val loadedGames: LiveData<ArrayList<Game>> = _loadedGames

    init {
        loadInitialGamesList()
    }

    private fun setLoading(isLoading: Boolean) {
        _loading.postValue(Event(isLoading))
        isLoadingNextPage = isLoading
    }

    // USES CASES
    // 1- Initial loading
    private fun loadInitialGamesList() {
        setLoading(true)
        actualPage = 1
        loadGamesList()
    }

    // 2- Refresh from swipeDown behaviour
    fun refreshGamesList() {
        setLoading(true)
        actualPage = 1
        loadGamesList()
    }

    // 2- Fetching next page when reaching the end of list
    fun fetchNextPage() {
        loadNextPage()
    }

    // 10 - Clicked on a game
    fun onGameClicked(gameClicked: Game) {
        val action = MainFragmentDirections
            .actionMainFragmentToGameDetailsFragment(
                gameClicked.id
            )
        _navigation.postValue(Event(NavigationCommand.To(action)))
    }

    private fun loadGamesList() = viewModelScope.launch {
        repository.loadRefreshGames(
            successFromNetwork = { isLastPage, gameList ->
                actualPage++
                _loadedGames.postValue(gameList)
                _lastPageReachedState.postValue(Event(isLastPage))
                _gamesLoadingState.postValue(Event(true))
            },
            successFromDatabase = { gameList ->
                _loadedGames.postValue(gameList)
                _lastPageReachedState.postValue(Event(true))
                _gamesLoadingState.postValue(Event(true))
            },
            error = {
                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))
                _gamesLoadingState.postValue(Event(false))
            }
        )
        setLoading(false)
    }

    private fun loadNextPage() = viewModelScope.launch {
        isLoadingNextPage = true
        repository.loadNextPage(
            actualPage,
            successFromNetwork = { isLastPage, gameList ->
                actualPage++
                _addedGames.postValue(gameList)
                _loadedGames.value?.addAll(gameList)
                _lastPageReachedState.postValue(Event(isLastPage))
                _gamesAddedState.postValue(Event(true))
            },
            successFromDatabase = { gameList ->
                _loadedGames.postValue(gameList)
                _lastPageReachedState.postValue(Event(true))
                _gamesLoadingState.postValue(Event(true))
            },
            error = {
                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))
                _gamesLoadingState.postValue(Event(false))
            }
        )
    }
}
