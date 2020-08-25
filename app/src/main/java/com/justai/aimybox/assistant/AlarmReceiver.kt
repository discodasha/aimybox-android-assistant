package com.justai.aimybox.assistant
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.justai.aimybox.components.L



class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        L.d("Alarm!!!!!!")
    }
}