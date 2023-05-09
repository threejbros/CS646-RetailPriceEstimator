package com.zybooks.retailpriceestimator

import android.content.Context
import android.media.SoundPool
import android.media.AudioAttributes

class SoundEffects private constructor(context: Context?){

    private var soundPool: SoundPool? = null
    private var calculateSoundId = 0

    companion object {
        private var instance: SoundEffects? = null

        fun getInstance(context: Context?): SoundEffects {
            if (instance == null) {
                instance = SoundEffects(context)
            }
            return instance!!
        }
    }

    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(attributes)
            .build()

        soundPool?.let {
            calculateSoundId = it.load(context, R.raw.cash_register, 1)
        }
    }

    fun playCalculateSound() {
        soundPool?.play(calculateSoundId,
            1f,       // left volume (0.0 to 1.0)
            1f,       // right volume (0.0 to 1.0)
            1,        // priority (0 = lowest)
            0,        // loop (0 = no loop, -1 = loop forever)
            1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
        instance = null
    }
}