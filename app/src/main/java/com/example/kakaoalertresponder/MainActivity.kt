package com.example.kakaoalertresponder

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val prefName = "kakao_prefs"
    private val keyChatRooms = "chat_rooms" // 여러 방을 콤마로 직렬화하여 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etRoom = findViewById<EditText>(R.id.etRoom) // 기존 방 이름 입력 EditText
        val prefs = getSharedPreferences(prefName, MODE_PRIVATE)

        // 저장된 값 표시(힌트/초기값)
        val saved = prefs.getString(keyChatRooms, "") ?: ""
        if (saved.isNotBlank()) etRoom.setText(saved)

        // 기존 “설정 저장” 버튼 클릭 리스너 안에 아래 로직을 사용해줘
        findViewById<android.view.View>(R.id.btnSave)?.setOnClickListener {
            val rawInput = etRoom.text?.toString() ?: ""
            val normalized = normalizeRoomsInput(rawInput)

            if (normalized.isBlank()) {
                Toast.makeText(this, "감시할 방 이름을 한 개 이상 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit().putString(keyChatRooms, normalized).apply()
            Toast.makeText(this, "감시 방 목록 저장 완료:\n$normalized", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 여러 구분자(쉼표, 세미콜론, 줄바꿈)를 허용하고
     * 공백/중복을 제거하여 "A, B, C" 형태로 직렬화
     */
    private fun normalizeRoomsInput(input: String): String {
        // 쉼표/세미콜론/줄바꿈/탭을 전부 구분자로 인식
        val tokens = input
            .split(',', ';', '\n', '\r', '\t')
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        // 대소문자 구분 없이 중복 제거 + 보기 좋게 정렬(선택)
        val unique = LinkedHashSet<String>()
        tokens.forEach { unique.add(it) }

        return unique.joinToString(separator = ", ")
    }
}
