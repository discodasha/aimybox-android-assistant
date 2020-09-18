package com.justai.aimybox.assistant

import android.app.Application
import android.content.Context
import com.justai.aimybox.Aimybox
import com.justai.aimybox.api.aimybox.AimyboxDialogApi
import com.justai.aimybox.components.AimyboxProvider
import com.justai.aimybox.core.Config
import com.justai.aimybox.speechkit.google.platform.GooglePlatformSpeechToText
import com.justai.aimybox.speechkit.google.platform.GooglePlatformTextToSpeech
import com.justai.aimybox.speechkit.kaldi.KaldiAssets
import com.justai.aimybox.speechkit.kaldi.KaldiVoiceTrigger
import java.util.*

class AimyboxApplication : Application(), AimyboxProvider {

    override val aimybox by lazy { createAimybox(this, "1234567890") }

    fun createAimybox(context: Context, unitId: String): Aimybox {
        val language = Locale.getDefault()
        val assets = KaldiAssets.fromApkAssets(this, "model/ru")

        val textToSpeech = GooglePlatformTextToSpeech(context, language) // Or any other TTS
        val speechToText = GooglePlatformSpeechToText(context, language) // Or any other STT
        val voiceTrigger = KaldiVoiceTrigger(assets, listOf("слушай", "кеша"))
        val dialogApi = AimyboxDialogApi("", unitId, "https://83e1c63238f9.ngrok.io")


        return Aimybox(Config.create(speechToText, textToSpeech, dialogApi) {
            this.voiceTrigger = voiceTrigger
        })
    }

}