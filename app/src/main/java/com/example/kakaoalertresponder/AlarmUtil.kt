package com.example.kakaoalertresponder

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

/**
 * 알림용 단순 Beep & 진동 유틸
 * - 외부 리소스/권한 최소화 (오디오 톤, 진동만 사용)
 * - NPE/오디오 정책 충돌 대비 try-catch 방어
 */
object AlarmUtil {

    private const val TAG = "AlarmUtil"
    private const val BEEP_DURATION_MS = 200
    private const val BEEP_STREAM = AudioManager.STREAM_ALARM
    private const val BEEP_TONE = ToneGenerator.TONE_PROP_BEEP

    @Volatile
    private var toneGen: ToneGenerator? = null

    /** 짧은 비프음 */
    @JvmStatic
    fun playBeep(context: Context? = null) {
        try {
            // 톤제너레이터가 죽었거나 릴리즈된 경우 재생성
            val tg = toneGen ?: ToneGenerator(BEEP_STREAM, /*volume*/ 80).also { toneGen = it }
            tg.startTone(BEEP_TONE, BEEP_DURATION_MS)
        } catch (t: Throwable) {
            Log.w(TAG, "playBeep failed: ${t.message}")
            // 오디오 정책 이슈 시 보조로 진동이라도 울려줌
            context?.let { vibrate(it, 120) }
        }
    }

    /** 짧은 진동 (기본 150ms) */
    @JvmStatic
    fun vibrate(context: Context, millis: Long = 150) {
        try {
            val vib = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(millis)
            }
        } catch (t: Throwable) {
            Log.w(TAG, "vibrate failed: ${t.message}")
        }
    }

    /** 액티비티/서비스 onDestroy 등에서 명시적으로 호출하면 깔끔 */
    @JvmStatic
    fun release() {
        try {
            toneGen?.release()
        } catch (_: Throwable) {}
        toneGen = null
    }
}
