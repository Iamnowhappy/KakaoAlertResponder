package com.example.kakaoalertresponder

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import android.util.Log

class KakaoNotificationListener : NotificationListenerService() {

    private val prefName = "kakao_prefs"
    private val keyChatRooms = "chat_rooms"

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        runCatching {
            if (sbn == null) return

            val pkg = sbn.packageName ?: return
            // 카카오톡 패키지 필터 (필요시 패키지명 최신 확인)
            if (pkg != "com.kakao.talk") return

            val extras = sbn.notification.extras
            val title = (extras.getCharSequence("android.title") ?: "").toString()
            val text  = (extras.getCharSequence("android.text")  ?: "").toString()

            if (title.isBlank() && text.isBlank()) return

            val rooms = loadWatchRooms()
            if (rooms.isEmpty()) return

            // 대소문자 무시 contains 매칭
            val matched = rooms.firstOrNull { room ->
                title.contains(room, ignoreCase = true)
            }

            if (matched != null) {
                Log.d("KakaoNL", "감시 대상 방 매칭: [$matched] title=$title text=$text")

                // ✅ 여기서 너의 기존 처리(알람, 사운드, 진동, 자동 응답 등)를 호출
                onWatchedRoomMatched(matched, title, text, sbn)
            }
        }.onFailure { e ->
            Log.e("KakaoNL", "onNotificationPosted error", e)
        }
    }

    private fun loadWatchRooms(): List<String> {
        val prefs = getSharedPreferences(prefName, MODE_PRIVATE)
        val raw = prefs.getString(keyChatRooms, "") ?: ""
        if (raw.isBlank()) return emptyList()

        // 저장 포맷: "A, B, C" → 분리/정리
        return raw
            .split(',', ';', '\n', '\r', '\t')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { it.lowercase() }
    }

    /**
     * 감시 방과 매칭되었을 때 수행할 실제 동작.
     * 기존 알림/사운드/자동 응답 로직이 있다면 여기서 호출해줘.
     */
    private fun onWatchedRoomMatched(
        matchedRoom: String,
        title: String,
        text: String,
        sbn: StatusBarNotification
    ) {
        // 예: 비프 3회, 진동, 토스트, 로그 등
        try {
            AlarmUtil.playBeep(this) // 예시: 네 프로젝트의 유틸 호출
            // NotificationHelper.showHeadsUp(this, "[$matchedRoom] $text") 등도 가능
        } catch (_: Throwable) { /* no-op */ }
    }
}
