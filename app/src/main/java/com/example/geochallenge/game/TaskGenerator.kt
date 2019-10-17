package com.example.geochallenge.game

class TaskGenerator :TaskService{


    val tasks: ArrayList<Task> = ArrayList()
    var iterator: MutableIterator<Task>

    init {

        tasks.addTask("Москва",55.75222,37.61556 )
            .addTask("Cанкт-Петербург",59.93863,  30.31413)
            .addTask( "Нью-Йорк", 40.71424, -74.00594)
            .addTask("Грозный", 43.31195, 45.68895)
            .addTask("Багдад", 33.34058, 44.40088)
            .addTask("Пекин",  39.9075, 116.39723)
            .addTask("Лондон", 51.50853, -0.12574)
            .addTask("Тунис", 36.81897, 10.16579)


        iterator =  tasks.iterator()

    }

    override fun nextTask(): Task? = if(iterator.hasNext()) iterator.next() else null


    private fun  ArrayList<Task>.addTask(name : String, latitude: Double, longitude: Double ) : ArrayList<Task>{
        val task = Task(name, latitude, longitude)
        this.add(task)
        return this
    }




}



