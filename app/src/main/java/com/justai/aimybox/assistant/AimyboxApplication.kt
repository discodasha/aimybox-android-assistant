package com.justai.aimybox.assistant

import android.app.Application
import android.content.Context
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxDialogApi
import com.justai.aimybox.assistant.custom.skills.AlarmSkill
import com.justai.aimybox.assistant.custom.skills.ReminderSkill
import com.justai.aimybox.assistant.custom.skills.TimerSkill
import com.justai.aimybox.components.AimyboxProvider
import com.justai.aimybox.core.Config
import com.justai.aimybox.speechkit.google.platform.GooglePlatformSpeechToText
import com.justai.aimybox.speechkit.google.platform.GooglePlatformTextToSpeech
import java.util.*

class AimyboxApplication : Application(), AimyboxProvider {

    companion object {
        private const val AIMYBOX_API_KEY = "3ZvQXLsAzlAjO8tBm6xP3a0PNDuVyzKw"
    }

    override val aimybox by lazy { createAimybox(this) }

    private fun createAimybox(context: Context): Aimybox {
        val unitId = UUID.randomUUID().toString()

        //val assets = KaldiAssets.fromApkAssets(this, "model/ru")

//        val textToSpeech = GooglePlatformTextToSpeech(context)
//        val speechToText = KaldiSpeechToText(assets)
//        val voiceTrigger = KaldiVoiceTrigger(assets, listOf("слушай"))

        val textToSpeech = GooglePlatformTextToSpeech(context,Locale("Ru"))
        val speechToText = GooglePlatformSpeechToText(context, Locale("Ru"))

        val dialogApi = AimyboxDialogApi(AIMYBOX_API_KEY, unitId,
            customSkills = linkedSetOf(
                AlarmSkill(context),
                TimerSkill(context),
                ReminderSkill(context)
            ))


//        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi) {
//            this.voiceTrigger = voiceTrigger
//        })

        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi))

//        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi))
    }

}