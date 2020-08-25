package com.justai.aimybox.assistant

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.components.AimyboxAssistantFragment
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response


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


    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val i = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT)
        var calendar: Calendar = Calendar.getInstance()
        calendar.set(2020, 8, 25, 20, 58, 0)

        val thirtySecondsFromNow = System.currentTimeMillis() + 30 * 1000
        am!!.setExact(AlarmManager.RTC_WAKEUP, thirtySecondsFromNow, pi)
    }
}

