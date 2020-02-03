package ru.geochallengegame.app.ui.game

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.TaskAnswer
import ru.geochallengegame.app.utils.CalculateUtils
import java.io.IOException

abstract class BaseGameViewModel : ViewModel() {

    companion object {
        const val DEFAULT_DISTANCE = 800.0
    }

    private val compositeDisposable = CompositeDisposable()
    val isDefaultMapState: MutableLiveData<Boolean> = MutableLiveData()
    val taskAnswer = MutableLiveData<TaskAnswer>()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask: MutableLiveData<CityTask> = MutableLiveData()
    val distance: MutableLiveData<Double> = MutableLiveData()
    val taskCounterLevel = MutableLiveData(0)
    val taskCounterGame = MutableLiveData(0)
    val maxCountTasksForLevel = MutableLiveData(0)
    val currentLevel: MutableLiveData<Int> = MutableLiveData()
    val isLevelFinished: MutableLiveData<Boolean> = MutableLiveData()
    val error = MutableLiveData<GameError>().also {
        it.value =
            GameError.NONE
    }
    val isErrorVisible = MutableLiveData<Boolean>()
    val isLoadingVisible = MutableLiveData<Boolean>()
    val isGameInfoVisible = MutableLiveData<Boolean>()
    val gameResult = MutableLiveData<Pair<Int, Boolean>>()

    var cityTask: CityTask? = null

    open fun newGame() {
        nextLevel()
    }

    open fun updateTasks() {
        addDisposable(prepareNewLevel(currentLevel.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doOnSuccess {
                error.postValue(GameError.NONE)
                maxCountTasksForLevel.postValue(it)
                isLevelFinished.postValue(false)
            }
            .subscribe({
                if (it > 0) nextTask()
                else finishGame()
            }, {
                resolveError(it)
            })
        )
    }
    protected open fun nextLevel() {
        val level = currentLevel.value ?: 0
        val newLevel = if (level == 0) 1 else level + 1
        addDisposable(prepareNewLevel(newLevel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { currentLevel.postValue(newLevel) }
            .doOnSubscribe { showLoading() }
            .doOnSuccess {
                error.postValue(GameError.NONE)
                maxCountTasksForLevel.postValue(it)
                isLevelFinished.postValue(false)
            }
            .subscribe({
                if (it > 0) nextTask()
                else finishGame()
            }, {
                resolveError(it)
            })
        )
    }

    protected open fun resolveError(e: Throwable) {

        when (e) {
            is HttpException -> error.postValue(GameError.SERVER_ERROR)
            is IOException -> error.postValue(GameError.CONNECTION_ERROR)
            else -> error.postValue(GameError.ANY)
        }
        showError()
    }

    open fun nextTask() {


        if (!haveTaskForCurrentLevel()) {
            levelFinished()
            return
        }
        addDisposable(getNextTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doOnSuccess {
                error.postValue(GameError.NONE)
            }
            .subscribe({
                onStartTask(it)
                showGameInfo()
            }, {
                resolveError(it)
            })
        )
    }

    open fun onStartTask(task: CityTask) {
        setTask(task)
        isDefaultMapState.postValue(true)
        clickedPosition.postValue(null)
        isTaskCompleted.postValue(false)
        taskAnswer.postValue(null)
        taskCounterLevel.postValue(taskCounterLevel.value?.plus(1) ?: 1)
        taskCounterGame.postValue(taskCounterGame.value?.plus(1) ?: 1)
    }

    open fun clickedPosition(latitude: Double, longitude: Double) {
        clickedPosition.postValue(Pair(latitude, longitude))
        val currentTaskLat = currentTask.value?.latitude ?: return
        val currentTaskLon = currentTask.value?.longitude ?: return
        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(currentTaskLat, currentTaskLon)
        ) / 1000 // m to km

        clickedPosition(latitude, longitude, d)
    }

    protected open fun clickedPosition(
        latitude: Double,
        longitude: Double, distance: Double
    ) {
        this.distance.postValue(distance)
//        val answer = TaskAnswer(LatLng(latitude, longitude), currentTask.value!!)
//        finishTask(answer)
    }

    protected open fun finishTask() {
        isTaskCompleted.postValue(true)
    }

    open fun finishGame() {
        isGameFinished.postValue(true)
    }

    protected open fun levelFinished() {
        isLevelFinished.postValue(true)
        taskCounterLevel.postValue(0)
    }


    @MainThread
    fun cameraMoved() {
        isDefaultMapState.value = false
    }

    private fun showGameInfo() {
        isLoadingVisible.postValue(false)
        isErrorVisible.postValue(false)
        isGameInfoVisible.postValue(true)
    }

    private fun showError() {
        isLoadingVisible.postValue(false)
        isErrorVisible.postValue(true)
        isGameInfoVisible.postValue(false)
    }

    private fun showLoading() {
        isLoadingVisible.postValue(true)
        isErrorVisible.postValue(false)
        isGameInfoVisible.postValue(false)
    }

    private fun setTask(task: CityTask) {
        this.cityTask = task
        this.currentTask.postValue(task)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun addDisposable(d: Disposable) {
        compositeDisposable.add(d)
    }

    abstract fun getNextTask(): Single<CityTask>
    abstract fun prepareNewLevel(newLevel: Int): Single<Int>


    abstract fun haveTaskForCurrentLevel(): Boolean

}