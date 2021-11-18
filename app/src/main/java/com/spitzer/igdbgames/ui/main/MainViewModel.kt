package com.spitzer.igdbgames.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.ui.pagination.PaginationResult
import com.spitzer.igdbgames.ui.pagination.PaginationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val paginationUseCases: PaginationUseCases
) : BaseViewModel() {

    private val _gamesAddedState = MutableLiveData<Event<Boolean>>()
    val gamesAddedState: LiveData<Event<Boolean>> = _gamesAddedState

    private val _gamesLoadingState = MutableLiveData<Event<Boolean>>()
    val gamesLoadingState: LiveData<Event<Boolean>> = _gamesLoadingState

    private val _lastPageReachedState = MutableLiveData<Event<Boolean>>()
    val lastPageReachedState: LiveData<Event<Boolean>> = _lastPageReachedState

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

    fun refreshGamesList() {
        loadInitialGamesList()
    }

    fun fetchNextPage() {
        loadNextPage()
    }

    fun onGameClicked(gameClicked: Game) {
        val action = MainFragmentDirections
            .actionMainFragmentToGameDetailsFragment(
                gameClicked.id
            )
        _navigation.postValue(Event(NavigationCommand.To(action)))
    }

    private fun loadInitialGamesList() = viewModelScope.launch {
        actualPage = 1
        setLoading(true)
        val paginationResult = paginationUseCases.fetchNextPage(actualPage)
        when (paginationResult) {
            is PaginationResult.Success -> {
                actualPage++
                if (paginationResult.data.isNotEmpty()) {
                    _loadedGames.postValue(paginationResult.data!!)
                    _gamesLoadingState.postValue(Event(true))
                }
                _lastPageReachedState.postValue(Event(paginationResult.paginationInfo.isLastPage))
            }
            is PaginationResult.Error -> {
                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))
                _gamesLoadingState.postValue(Event(false))
            }
        }
        setLoading(false)
    }

    private fun loadNextPage() = viewModelScope.launch {
        isLoadingNextPage = true
        val paginationResult = paginationUseCases.fetchNextPage(actualPage)
        when (paginationResult) {
            is PaginationResult.Success -> {
                actualPage++
                if (paginationResult.data.isNotEmpty()) {
                    if (paginationResult.paginationInfo.isFullDataRetrieved) {
                        _loadedGames.postValue(paginationResult.data!!)
                        _gamesLoadingState.postValue(Event(true))
                    } else {
                        _addedGames.postValue(paginationResult.data!!)
                        _loadedGames.value?.addAll(paginationResult.data!!)
                        _gamesAddedState.postValue(Event(true))
                    }
                }
                _lastPageReachedState.postValue(Event(paginationResult.paginationInfo.isLastPage))
            }
            is PaginationResult.Error -> {
                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))
                _gamesLoadingState.postValue(Event(false))
            }
        }

    }

    private fun setLoading(isLoading: Boolean) {
        _loading.postValue(Event(isLoading))
        isLoadingNextPage = isLoading
    }
}
