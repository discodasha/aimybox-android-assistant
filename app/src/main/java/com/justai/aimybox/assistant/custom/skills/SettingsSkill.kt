package com.justai.aimybox.assistant.custom.skills

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxRequest
import com.justai.aimybox.api.aimybox.AimyboxResponse
import com.justai.aimybox.core.CustomSkill
import com.justai.aimybox.model.Response


class SettingsSkill(context_: Context) : CustomSkill<AimyboxRequest, AimyboxResponse> {

    override fun canHandle(response: AimyboxResponse) = response.action == "openSettings"
    private var context: Context = context_

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun onResponse(
        response: AimyboxResponse,
        aimybox: Aimybox,
        defaultHandler: suspend (Response) -> Unit
    ) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.applicationContext.startActivity(intent)
    }

}

