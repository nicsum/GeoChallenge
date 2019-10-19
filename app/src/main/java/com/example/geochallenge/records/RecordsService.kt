package com.example.geochallenge.records

import com.example.geochallenge.game.Record
import io.reactivex.Single

interface RecordsService {



    fun insertRecord(record: Record)
    fun getRecords(): Single<MutableList<Record>>
}