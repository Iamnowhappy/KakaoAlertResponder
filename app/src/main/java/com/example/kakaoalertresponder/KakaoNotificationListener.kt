package com.example.kakaoalertresponder

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class KakaoNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != "com.kakao.talk") return

        val prefs = getSharedPreferences("alert_prefs", MODE_PRIVATE)
        val targetRoom = prefs.getString("target_room", "") ?: ""
        val targetSender = prefs.getString("target_sender", "") ?: ""

        val extras = sbn.notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getString(Notification.EXTRA_TEXT) ?: ""

        val roomMatches = title.contains(targetRoom)
        val senderMatches = if (targetSender.isNotEmpty()) {
            text.contains(targetSender)
        } else {
            true // 🔥 sender가 비어있으면 전체 감지
        }

        if (roomMatches && senderMatches && targetRoom.isNotEmpty()) {
            Log.d("KakaoAlert", "✅ 감지됨 — 방: $targetRoom / 발신자: ${if (targetSender.isEmpty()) "전체" else targetSender}")

            NotificationHelper.showActionNotification(
                context = this,
                title = "자동응답 준비",
                message = "예 알겠습니다 수고하세요"
            )

            TonePlayer.playBeepTriplet(this)
            AlarmUtil.scheduleNextAlert(this)
        }
    }
}
