package com.example.geochallenge.ui.game

import androidx.lifecycle.MutableLiveData

class DistanceLimitGameViewModel : SimpleGameViewModel() {

    companion object{
        const val DISTANCE_LIMIT = 5_000
    }

    val stillHaveDistance = MutableLiveData<Int>()


    override fun newGame(){
        super.newGame()
        stillHaveDistance.postValue(DISTANCE_LIMIT)
    }

    override fun nextTask() {
        super.nextTask()
        val result = stillHaveDistance.value?.minus(distance.value ?: 0) ?: DISTANCE_LIMIT
        if(result < 0) finishGame()
        else stillHaveDistance.postValue(result)
    }

}