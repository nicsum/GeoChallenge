package com.example.geochallenge.records

import android.util.Log
import com.example.geochallenge.data.GeoChallengeDao
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordsStorage(val dao: GeoChallengeDao) : RecordsService{
    override fun insertRecord(record: Record) {
        Single
            .just(record)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    dao.insertRecord(it)
                },
                {
                    Log.d("database", it.message)
                })

    }

    override fun getRecords() :Single<MutableList<Record>>{

        return Single
            .fromCallable { dao.getRecords() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

//        return
    }


}