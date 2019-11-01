package com.example.geochallenge.game.multiplayer

import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable

class MultiplayerControler {



    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("sessions")


    fun startGame() : Completable{

        TODO()
    }

}