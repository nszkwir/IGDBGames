package com.spitzer.igdbgames.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spitzer.igdbgames.R
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.ui.pagination.PaginationMediator
import com.spitzer.igdbgames.ui.pagination.PaginationResult
import com.spitzer.igdbgames.ui.pagination.PaginationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val paginationUseCases: PaginationUseCases
) : BaseViewModel() {

    private val paginationMediator: PaginationMediator = PaginationMediator()

    private val _paginationViewState = MutableLiveData<Event<MainViewState>>()
    val paginationViewState: LiveData<Event<MainViewState>> = _paginationViewState

    private var actualPage = 1

    private val _gamesList = MutableLiveData<ArrayList<Game>>()
    val gamesList: LiveData<ArrayList<Game>> = _gamesList

    init {
        loadInitialGamesList()
    }

    fun processViewEvent(viewEvent: MainViewEvent) {
        when (viewEvent) {
            is MainViewEvent.PaginationEvent.FetchNextPageEvent -> {
                paginationMediator.isLoadingNextPage = true
                paginationMediator.isRefreshing = false
                loadNextPage()
            }
            is MainViewEvent.PaginationEvent.RefreshGameListEvent -> {
                actualPage = 1
                paginationMediator.isLoadingNextPage = false
                paginationMediator.isLastPage = false
                paginationMediator.isRefreshing = true
                refreshInitialGameList()
            }
//            is MainViewEvent.PaginationEvent.RetryFetchNextPageEvent -> { // TODO
//            }
            is MainViewEvent.GoToGameDetails -> {
                val action = MainFragmentDirections
                    .actionMainFragmentToGameDetailsFragment(
                        viewEvent.gameId
                    )
                _navigation.postValue(Event(NavigationCommand.To(action)))
            }
        }
    }

    private fun loadInitialGamesList() = viewModelScope.launch {

        setLoading(true)

        val paginationResult = paginationUseCases.fetchNextPage(actualPage)

        when (paginationResult) {

            is PaginationResult.Success -> {

                actualPage++

                if (paginationResult.data.isNotEmpty()) {

                    _gamesList.postValue(paginationResult.data!!)
                    _paginationViewState.postValue(
                        Event(
                            MainViewState.LoadingInitialGameList(
                                true,
                                paginationResult.paginationInfo.isLastPage
                            )
                        )
                    )

                } else {
                    // TODO define and manage this state
                }
            }
            is PaginationResult.Error -> {

                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))

                _paginationViewState.postValue(
                    Event(
                        MainViewState.LoadingInitialGameList(
                            false,
                            false
                        )
                    )
                )
            }
        }
        setLoading(false)
    }

    private fun refreshInitialGameList() = viewModelScope.launch {

        val paginationResult = paginationUseCases.fetchNextPage(actualPage)

        when (paginationResult) {

            is PaginationResult.Success -> {

                actualPage++

                if (paginationResult.data.isNotEmpty()) {

                    _gamesList.postValue(paginationResult.data!!)
                    _paginationViewState.postValue(
                        Event(
                            MainViewState.RefreshingGameList(
                                true,
                                paginationResult.paginationInfo.isLastPage
                            )
                        )
                    )

                } else {
                    // TODO define and manage this state
                }
            }
            is PaginationResult.Error -> {

                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))

                _paginationViewState.postValue(
                    Event(
                        MainViewState.RefreshingGameList(
                            false,
                            false
                        )
                    )
                )
            }
        }
    }

    private fun loadNextPage() = viewModelScope.launch {

        when (val paginationResult = paginationUseCases.fetchNextPage(actualPage)) {

            is PaginationResult.Success -> {

                actualPage++

                if (paginationResult.data.isNotEmpty()) {

                    if (paginationResult.paginationInfo.isFullDataRetrieved) {

                        _gamesList.postValue(paginationResult.data!!)

                        paginationMediator.isLastPage = paginationResult.paginationInfo.isLastPage
                        _paginationViewState.postValue(
                            Event(
                                MainViewState.AddedAllGamesFromDb(
                                    true,
                                    true
                                )
                            )
                        )
                    } else {
                        val position = gamesList.value?.size ?: 0
                        _gamesList.value?.addAll(paginationResult.data!!)
                        _paginationViewState.postValue(
                            Event(
                                MainViewState.AddNextGamePageToList(
                                    true,
                                    paginationResult.paginationInfo.isLastPage,
                                    position,
                                    paginationResult.data.size
                                )
                            )
                        )
                    }
                } else {
                    // TODO define and manage this state
                    // RetryFetchNextPage viewState must be defined
                    // and handled by MainFragment to set the retry button as visible
                }
            }
            is PaginationResult.Error -> {
                _snackbarError.postValue(Event(R.string.snackbar_could_not_fetch))
                _paginationViewState.postValue(
                    Event(
                        MainViewState.AddNextGamePageToList(
                            false,
                            false,
                            0,
                            0
                        )
                    )
                )
            }
        }

    }

    private fun setLoading(isLoading: Boolean) {
        _loading.postValue(Event(isLoading))
    }

    fun lastPageReached() = paginationMediator.isLastPage
    fun isLoading() = paginationMediator.isLoadingNextPage || paginationMediator.isRefreshing
    fun isLoadingNextPage() = paginationMediator.isLoadingNextPage
    fun isRefreshing() = paginationMediator.isRefreshing

    fun nextPageLoaded() {
        paginationMediator.isLoadingNextPage = false
    }

    fun refreshFinished() {
        paginationMediator.isRefreshing = false
    }
}
