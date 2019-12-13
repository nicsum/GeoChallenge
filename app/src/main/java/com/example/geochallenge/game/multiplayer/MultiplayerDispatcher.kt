package com.example.geochallenge.game.multiplayer

import io.reactivex.Completable

interface MultiplayerDispatcher {

    fun startGame(listener: GameStateChangeListener)
    fun postAnswer(coordinates: Pair<Double, Double>): Completable
}