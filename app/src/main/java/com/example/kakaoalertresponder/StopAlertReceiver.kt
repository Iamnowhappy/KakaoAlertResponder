package com.example.kakaoalertresponder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class StopAlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // 모든 예약 취소 + 알림 제거
        AlarmUtil.cancel(context)
        NotificationHelper.cancel(context)
        Toast.makeText(context, "⛔ 반복 경고가 중지되었습니다.", Toast.LENGTH_SHORT).show()
    }
}