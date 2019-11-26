package com.example.geochallenge.di

import com.example.geochallenge.ui.game.GameMapFragment


interface MapComponent {

    fun inject(fragment: GameMapFragment)
}