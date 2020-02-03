package ru.geochallengegame.app.ui.menu.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.geochallengegame.app.data.GeochallengeService
import ru.geochallengegame.app.game.GameMap

class MenuMapsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val geochallengeService: GeochallengeService
) : ViewModel() {

    private var mode: String? = null
    private val compositeDisposable = CompositeDisposable()
    val maps = MutableLiveData<List<GameMap>>()
    val loadingIsVisible = MutableLiveData<Boolean>()
    val errorIsVisible = MutableLiveData<Boolean>()
    val isSignOut = MutableLiveData<Boolean>()
    val gameInfoIsVisible = MutableLiveData<Boolean>()


    fun loadMaps() {
        compositeDisposable.add(geochallengeService
            .getMaps(mode!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingIsVisible.postValue(true) }
            .doFinally { loadingIsVisible.postValue(false) }
            .subscribe({
                maps.postValue(it.sortedBy { task -> task.id })
                errorIsVisible.postValue(false)
            }, {
                errorIsVisible.postValue(true)
            })
        )
    }

    fun logout() {
        firebaseAuth.signOut()
        isSignOut.postValue(true)
    }

    fun info() {
        gameInfoIsVisible.postValue(true)
    }

    fun selectMode(mode: String?) {
        this.mode = mode
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun iReadModeInfo() {
        gameInfoIsVisible.postValue(false)
    }
}