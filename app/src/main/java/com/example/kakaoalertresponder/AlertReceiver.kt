package com.example.kakaoalertresponder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("KakaoAlert", "⚠️ 반복 경고 트리거 — 경고음 재생 & 다음 예약")

        // 경고음 재생 (무음 모드 무시)
        TonePlayer.playBeepTriplet(context)

        // 다음 알람 재등록 (1시간 후 반복)
        AlarmUtil.scheduleNextAlert(context)
    }
}