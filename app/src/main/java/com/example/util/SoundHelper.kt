package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundHelper(private val context: Context) {

    private var toneGenerator: ToneGenerator? = null
    private var ambientTrack: AudioTrack? = null
    private var ambientJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            toneGenerator = null
        }
    }

    fun playTick() {
        vibrate(20)
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
        } catch (_: Exception) {}
    }

    fun playCoin() {
        playCoinClink()
    }

    fun playCoinClink() {
        vibrate(40)
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 80)
                delay(90)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 120)
            } catch (_: Exception) {}
        }
    }

    fun playLevelUp() {
        playLevelUpFanfare()
    }

    fun playQuestComplete() {
        vibratePattern(longArrayOf(0, 50, 50, 80))
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 150)
                delay(160)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 250)
            } catch (_: Exception) {}
        }
    }

    fun playLevelUpFanfare() {
        vibratePattern(longArrayOf(0, 80, 60, 100, 60, 200))
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 120)
                delay(140)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 160)
                delay(180)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 400)
            } catch (_: Exception) {}
        }
    }

    fun startAmbientSound(frequency: Float) {
        stopAmbientSound()
        if (frequency <= 0f) return

        ambientJob = scope.launch {
            try {
                val sampleRate = 22050
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(4096)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                ambientTrack = track
                track.play()

                val buffer = ShortArray(bufferSize / 2)
                var phase = 0.0

                while (isActive) {
                    for (i in buffer.indices) {
                        val modulation = 1.0 + 0.15 * sin(2.0 * Math.PI * 0.5 * (phase / sampleRate))
                        val wave = sin(2.0 * Math.PI * frequency * modulation * (phase / sampleRate))
                        val softNoise = (Math.random() * 2.0 - 1.0) * 0.12
                        buffer[i] = ((wave * 0.4 + softNoise) * 6000.0).toInt().toShort()
                        phase += 1.0
                    }
                    track.write(buffer, 0, buffer.size)
                }
            } catch (_: Exception) {
            }
        }
    }

    fun stopAmbientSound() {
        ambientJob?.cancel()
        ambientJob = null
        try {
            ambientTrack?.stop()
            ambientTrack?.release()
        } catch (_: Exception) {}
        ambientTrack = null
    }

    private fun vibrate(millis: Long) {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(millis)
            }
        } catch (_: Exception) {}
    }

    private fun vibratePattern(pattern: LongArray) {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (_: Exception) {}
    }

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }
}
