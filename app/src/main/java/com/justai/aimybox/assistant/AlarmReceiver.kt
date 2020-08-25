package com.justai.aimybox.assistant
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.justai.aimybox.components.L


class AlarmReceiver: BroadcastReceiver() {
    @SuppressLint("ShowToast")
    override fun onReceive(context: Context?, intent: Intent?) {
        L.d("Alarm!!!!!!")
        Toast.makeText(context, "whats up bitch", Toast.LENGTH_LONG).show()
    }



}