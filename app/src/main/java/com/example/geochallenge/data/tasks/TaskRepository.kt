package com.example.geochallenge.data.tasks

import com.example.geochallenge.game.CityTask
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

import io.reactivex.SingleOnSubscribe
import java.lang.NullPointerException


class TaskRepository() : TaskService {
    override fun getRandomCityTasksByLevel(level: Int, count: Int): Single<MutableList<CityTask>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cities")
    var nextCityIndex = 0


    override fun getAllCityTasksByLevel(level: Int): Single<MutableList<CityTask>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

     fun nextTask(): Single<CityTask> {


        val l = SingleOnSubscribe<CityTask> { singleEmitter ->
            val cities = myRef.child(nextCityIndex.toString())
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val task = dataSnapshot.getValue(CityTask::class.java)
                    if(task != null){
                        nextCityIndex++
                        singleEmitter.onSuccess(task)
                    }
                    else throw NullPointerException()
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    singleEmitter.onError(databaseError.toException())
                }
            }
            cities.addListenerForSingleValueEvent(postListener)
        }

        return  Single.create<CityTask>(l)
    }


}