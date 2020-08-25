package com.justai.aimybox.assistant

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.components.AimyboxAssistantFragment
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response
import java.time.LocalDateTime
import java.time.ZoneOffset


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val assistantFragment = AimyboxAssistantFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.assistant_container, assistantFragment)
            commit()
        }
    }

    override fun onBackPressed() {
        val assistantFragment = (supportFragmentManager.findFragmentById(R.id.assistant_container)
                as? AimyboxAssistantFragment)
        if (assistantFragment?.onBackPressed() != true) super.onBackPressed()
    }

}

class AlarmSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "setAlarm"
    private var context: Context = context_


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {

        val type = response.data?.asJsonObject?.get("type")?.asString ?: ""
        val message = response.data?.asJsonObject?.get("text")?.asString ?: "defmes"
        val timestamp = response.data?.asJsonObject?.get("timestamp")?.asLong ?: 0

        when (type) {
            "timer" -> createTimer(message, timestamp)
            "alarm" -> createAlarm(message, timestamp)
            "reminder" -> createReminder(message, timestamp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAlarm(message: String, timestamp: Long) {

        val time = LocalDateTime.ofEpochSecond(timestamp/1000, 0, ZoneOffset.UTC)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, time.hour)
            putExtra(AlarmClock.EXTRA_MINUTES, time.minute)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
            putExtra(AlarmClock.EXTRA_DAYS, ArrayList<Int>(time.dayOfWeek.value))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.callImplicitIntent(intent)
    }

    private fun createTimer(message: String, seconds: Long) {

        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        this.callImplicitIntent(intent)
    }

    private fun callImplicitIntent(intent: Intent) {
        if (intent.resolveActivity(context.packageManager) != null)
            context.applicationContext.startActivity(intent)
    }


    private fun createReminder(title: String, ts: Long) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, title)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, ts)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        this.callImplicitIntent(intent)
    }

}
