package com.justai.aimybox.assistant.custom.skills

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import androidx.annotation.RequiresApi
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.assistant.utilities.callImplicitIntent
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response
import java.time.LocalDateTime
import java.time.ZoneOffset


class AlarmSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "setAlarm"
    private var context: Context = context_

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val responseData = response.data?.asJsonObject
        val message = responseData?.get("text")?.asString ?: "Wake up, buddy!"
        val timestamp = responseData?.get("timestamp")?.asLong ?: 0

        if (responseData?.get("isRecurrent")?.asBoolean!!) {
            this.createRecurrentAlarm(responseData.get("recurrentDay").asInt, timestamp)
        } else
            this.createOnetimeAlarm(message, timestamp)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createOnetimeAlarm(message: String, timestamp: Long) {
        val t: ZoneOffset = ZoneOffset.of("+03:00")

        val time = LocalDateTime.ofEpochSecond(timestamp/1000, 0, t)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, time.hour)
            putExtra(AlarmClock.EXTRA_MINUTES, time.minute)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callImplicitIntent(intent, context)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRecurrentAlarm(recurrence: Int, timestamp: Long) {
        val t: ZoneOffset = ZoneOffset.of("+03:00")

        val time = LocalDateTime.ofEpochSecond(timestamp/1000, 0, t)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(recurrence))
            putExtra(AlarmClock.EXTRA_HOUR, time.hour)
            putExtra(AlarmClock.EXTRA_MINUTES, time.minute)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callImplicitIntent(intent, context)
    }
}

