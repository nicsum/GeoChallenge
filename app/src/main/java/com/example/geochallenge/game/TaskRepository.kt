package com.example.geochallenge.game

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

import io.reactivex.SingleOnSubscribe
import java.lang.NullPointerException


class TaskRepository : TaskService {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cities")
    var nextCityIndex = 0


    override fun getTasksForLevel(level: Int): Single<List<Task>?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nextTask(): Single<Task?> {

        val l = SingleOnSubscribe<Task> {singleEmitter ->
            val cities = myRef.child(nextCityIndex.toString())
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val task = dataSnapshot.getValue(Task::class.java)
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

        return  Single.create<Task>(l)
    }


}