package com.example.geochallenge.game.multiplayer

interface GameStateChangeListener {

    fun onNextTask(taskId: Int)
    fun onStartGame(taskId: Int)
    fun onPlayerMakeAnswer(playersAnswer: Map<String, Pair<Double, Double>?>)
    fun onFinishGame()
    fun onFailStartGame(throwable: Throwable)
    fun onFailContinueGame(throwable: Throwable)
}