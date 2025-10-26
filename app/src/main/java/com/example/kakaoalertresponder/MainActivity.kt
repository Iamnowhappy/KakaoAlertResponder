package com.example.kakaoalertresponder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etRoom: EditText
    private lateinit var etSender: EditText
    private val prefsName = "alert_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        etRoom = findViewById(R.id.etRoom)
        etSender = findViewById(R.id.etSender)

        // 기존 저장값 불러오기
        etRoom.setText(prefs.getString("target_room", "프로젝트방"))
        etSender.setText(prefs.getString("target_sender", ""))

        findViewById<Button>(R.id.btnSaveSetting).setOnClickListener {
            val room = etRoom.text.toString().trim()
            val sender = etSender.text.toString().trim()
            if (room.isEmpty()) {
                Toast.makeText(this, "방 이름은 반드시 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            prefs.edit()
                .putString("target_room", room)
                .putString("target_sender", sender)
                .apply()
            Toast.makeText(this, "설정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 기존 버튼들
        findViewById<Button>(R.id.btnOpenNLSettings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        findViewById<Button>(R.id.btnOpenExactAlarm).setOnClickListener {
            startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
        findViewById<Button>(R.id.btnIgnoreBattery).setOnClickListener {
            startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
        }
    }
}
