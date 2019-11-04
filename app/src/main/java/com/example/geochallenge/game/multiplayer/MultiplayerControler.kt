package com.example.geochallenge.game.multiplayer


import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class MultiplayerControler {

    val database = FirebaseFirestore.getInstance()


    fun startGame() : Completable?{

        database
            .collection("sessions")
            .whereEqualTo("status", 0)
            .get()
            .addOnCompleteListener {
            it.isSuccessful
        }
        return null
    }

}