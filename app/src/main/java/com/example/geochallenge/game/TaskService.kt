package com.example.geochallenge.game

import io.reactivex.Single

interface TaskService {

    fun nextTask(): Single<Task?>

    fun getTasksForLevel(level: Int): Single<List<Task>?>
}