package com.example.geochallenge.ui.menu.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameMap
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MenuMapsViewModel(
    private val firebaseAuth: FirebaseAuth,
    private val geochallengeService: GeochallengeService
) : ViewModel() {

    val maps = MutableLiveData<List<GameMap>>()
    val loadingIsVisible = MutableLiveData<Boolean>()
    val errorIsVisible = MutableLiveData<Boolean>()
    val isSignOut = MutableLiveData<Boolean>()

    fun loadMaps() {
        geochallengeService
            .getMaps()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingIsVisible.postValue(true) }
            .doFinally { loadingIsVisible.postValue(false) }
            .subscribe({
                maps.postValue(it)
                errorIsVisible.postValue(false)
            }, {
                errorIsVisible.postValue(true)
            })
    }

    fun logout() {
        firebaseAuth.signOut()
        isSignOut.postValue(true)
    }
}