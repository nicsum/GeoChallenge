package com.example.geochallenge.game.multiplayer

class GameState(var id: Int,
                var status: Int,
                var tasks: List<Int>,
                var progress: Map<String, Int>)