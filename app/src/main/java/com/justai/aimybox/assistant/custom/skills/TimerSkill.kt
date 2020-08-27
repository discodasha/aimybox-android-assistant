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


class TimerSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "setTimer"
    private var context: Context = context_

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val message = response.data?.asJsonObject?.get("text")?.asString ?: "AssistantTimer!"
        val timestamp = response.data?.asJsonObject?.get("duration")?.asInt ?: 0

        this.createTimer(message, timestamp)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createTimer(message: String, seconds: Int) {

        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        callImplicitIntent(intent, context)
    }

}

