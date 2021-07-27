package com.spitzer.igdbgames.ui.gamedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spitzer.igdbgames.core.BaseViewModel
import com.spitzer.igdbgames.core.Event
import com.spitzer.igdbgames.repository.data.Game

class GameDetailsViewModel : BaseViewModel() {

    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _viewState = MutableLiveData<Event<Boolean>>()
    val viewState: LiveData<Event<Boolean>> = _viewState

    fun setGameModel(gameModel: Game) {
        _loading.value = Event(true)
        _game.value = gameModel
        // TODO: define states to give ui responsiveness
        _viewState.value = Event(true)
    }

    fun viewLoaded() {
        _loading.value = Event(false)
    }
}
