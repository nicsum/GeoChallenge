package com.example.geochallenge.data

import androidx.room.*
import com.example.geochallenge.game.Record


@Dao
interface GeoChallengeDao {

    @Query("select * from records")
    fun getRecords(): MutableList<Record>


    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun insertRecord(record: Record)
}