package net.dixq.overlaypainter

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PaintService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}