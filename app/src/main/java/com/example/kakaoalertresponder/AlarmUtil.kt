package com.example.kakaoalertresponder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log

object AlarmUtil {
    private const val REQ_CODE = 10021

    fun scheduleNextAlert(context: Context) {
        val prefs = context.getSharedPreferences("alert_prefs", Context.MODE_PRIVATE)
        val intervalMinutes = prefs.getInt("interval_minutes", 60)
        val intervalMillis = intervalMinutes * 60 * 1000L

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context, REQ_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAt = System.currentTimeMillis() + intervalMillis
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pi)
        Log.d("KakaoAlert", "🔁 ${intervalMinutes}분 후 다음 경고 예약 완료: $triggerAt")
    }

    fun cancel(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlertReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            context, REQ_CODE, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        am.cancel(pi)
        Log.d("KakaoAlert", "⛔ 모든 경고 예약 취소")
    }
}
