package com.spitzer.igdbgames.ui.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.core.NavigationCommand
import com.spitzer.igdbgames.repository.GameDetailsRepository
import com.spitzer.igdbgames.repository.data.Game
import com.spitzer.igdbgames.repository.data.GameVideo
import com.spitzer.igdbgames.repository.data.LocalRating
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val repository: GameDetailsRepository
) : BaseViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _rating = MutableLiveData<LocalRating>()
    val rating: LiveData<LocalRating> = _rating

    private val _dataLoaded = MutableLiveData<Event<Boolean>>()
    val dataLoaded: LiveData<Event<Boolean>> = _dataLoaded

    private val _localRatingUpdated = MutableLiveData<Event<Boolean>>()
    val localRatingUpdated: LiveData<Event<Boolean>> = _localRatingUpdated

    // USES CASES
    // 1- Load model data from Database
    fun loadGameData(gameId: Int) {
        setLoading(true)
        getGame(gameId)
    }

    private fun getGame(gameId: Int) = viewModelScope.launch {
        repository.getGame(
            gameId = gameId,
            successFromDatabase = { game, rating ->
                _game.postValue(game)
                _rating.postValue(rating)
                _dataLoaded.postValue(Event(true))
            },
            error = {
                _dataLoaded.postValue(Event(false))
            })
    }

    fun updateLocalRating(newRatingValue: Float) = viewModelScope.launch {
        game.value?.id?.let {
            _rating.postValue(LocalRating(game.value!!.id!!, newRatingValue.toDouble()))
            repository.udpateLocalRating(
                gameId = game.value!!.id,
                localRating = newRatingValue.toDouble(),
                success = {
                    _localRatingUpdated.postValue(Event(true))
                },
                error = {
                    _localRatingUpdated.postValue(Event(false))
                }
            )

        } ?: run {
            _localRatingUpdated.postValue(Event(false))
        }
    }

    fun setLoading(isLoading: Boolean) {
        _loading.postValue(Event(isLoading))
    }

    fun navigateToVideoFragment(gameVideo: GameVideo) {
        val action = GameDetailsFragmentDirections
            .actionGameDetailsFragmentToVideoFragment(
                gameVideo.videoId
            )
        _navigation.postValue(Event(NavigationCommand.To(action)))
    }
}
