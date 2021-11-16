package com.spitzer.igdbgames.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.retrofit.GamesRepository
import com.spitzer.igdbgames.repository.room.GameDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val database: GameDatabase,
    repository: GamesRepository
) : BaseViewModel() {

    private val _games = MutableLiveData<ArrayList<Game>>()
    val games: LiveData<ArrayList<Game>> = _games

    //    private val pagingSourceFactory = { database.gamesDao().allGames() }
//
//    @ExperimentalPagingApi
//    val gameList: Flow<PagingData<Game>> =
//        Pager(
//            config = PagingConfig(
//                pageSize = GamesRepositoryImpl.GAME_FETCH_NUMBER,
//                prefetchDistance = 2
//            ),
//            remoteMediator = GameRemoteMediator(
//                repository,
//                database
//            ),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow
//
    fun onGameClicked(gameClicked: Game) {
        val action = MainFragmentDirections
            .actionMainFragmentToGameDetailsFragment(
                gameClicked
            )
        _navigation.value = Event(NavigationCommand.To(action))
    }
//
//    fun manageLoadingStates(loadStates: CombinedLoadStates) {
//        when {
//            loadStates.refresh is LoadState.NotLoading -> _loading.value = Event(false)
//            loadStates.refresh is LoadState.Loading -> _loading.value = Event(true)
//            loadStates.refresh is LoadState.Error -> _loading.value = Event(false)
//
//            // Showing error
//            loadStates.source.append is LoadState.Error ||
//                    loadStates.append is LoadState.Error ||
//                    loadStates.source.prepend is LoadState.Error ||
//                    loadStates.prepend is LoadState.Error -> {
//                _loading.value = Event(false)
//                _snackbarError.value = Event(R.string.error_generic)
//            }
//        }
//    }
}
