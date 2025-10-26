package com.example.kakaoalertresponder

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object TonePlayer {
    private var soundPool: SoundPool? = null
    private var beepSoundId: Int = 0

    fun playBeepTriplet(context: Context) {
        if (soundPool == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build()

            // res/raw/beep.wav 파일이 필요함
            beepSoundId = soundPool!!.load(context, R.raw.beep, 1)
        }

        soundPool?.let { sp ->
            for (i in 0..2) {
                sp.play(beepSoundId, 1f, 1f, 0, 0, 1f)
                Thread.sleep(300)
            }
        }
    }
}
