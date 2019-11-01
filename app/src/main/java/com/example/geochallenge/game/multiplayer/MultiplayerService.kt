package com.example.geochallenge.game.multiplayer

import android.app.IntentService
import android.content.Intent
import android.os.IBinder

class MultiplayerService : IntentService("MultiplayerService") {




    override fun onHandleIntent(intent: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }
}