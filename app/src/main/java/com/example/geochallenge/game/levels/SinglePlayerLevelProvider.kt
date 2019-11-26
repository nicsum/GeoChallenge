package com.example.geochallenge.game.levels

import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.CityTask
import io.reactivex.Completable
import io.reactivex.Single

class SinglePlayerLevelProvider(val taskService: TaskService, val countTaskForLevel: Int = 5) :
    LevelProvider {


    lateinit var iteratorTasksForCurrentLevel: Iterator<CityTask>

    override fun getNextTask(): Single<CityTask> {
        return Single.just(iteratorTasksForCurrentLevel.next())
    }

    override fun prepareForLevel(level: Int): Completable {
        return taskService.getRandomCityTasksByLevel(
            level,
            countTaskForLevel
        )
            .flatMapCompletable {
                iteratorTasksForCurrentLevel = it.iterator()
                Completable.complete()
            }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())

//            .subscribe({
//                iteratorTasksForCurrentLevel = it.iterator()
//                currentLevel.postValue(newLevel)
//                isLevelFinished.postValue(false)
//                nextTask()
//            },{
//                Log.e("SimpleGameViewModel",it.message)
//            })
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return iteratorTasksForCurrentLevel.hasNext()
    }

}