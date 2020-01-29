package com.example.geochallenge.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthViewModelFactory
@Inject constructor(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(FirebaseAuth::class.java, FirebaseFirestore::class.java)
            .newInstance(firebaseAuth, db)
    }


}