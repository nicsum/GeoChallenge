package com.example.geochallenge.game.multiplayer


import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class MultiplayerControler {

    /*TODO 1.отправляем запрос в firebase на новую игру (передаём лист тасков), в ответ получаем
       id gameState и свой id
       2. наблюдаем за gameState
       3. ждем когда state = "go task" и отображаем первый таск
       4. отправляем запрос в firebase с ответом состоящий из долготы и широты
       5. ждем когда state = "task finished"
       6. повторяем 2-6 пока gameState != "game finished"
    */

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