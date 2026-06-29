package com.example.projectttt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class LaporanTerkirimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_terkirim)

        val nomorId         = intent.getStringExtra("nomor_id") ?: "#SR-2026-0000"
        val btnBack         = findViewById<ImageView>(R.id.btnBackTerkirim)
        val btnKembaliBeranda= findViewById<Button>(R.id.btnKembaliBeranda)
        val btnLaporBaru    = findViewById<Button>(R.id.btnLaporBaru)
        val cardLacakStatus = findViewById<CardView>(R.id.cardLacakStatus)
        val tvNomorId       = findViewById<TextView>(R.id.tvNomorIdTerkirim)

        tvNomorId?.text = "Nomor ID: $nomorId"

        cardLacakStatus.setOnClickListener {
            startActivity(Intent(this, StatusActivity::class.java))
        }

        btnBack.setOnClickListener { finish() }

        btnKembaliBeranda.setOnClickListener {
            startActivity(
                Intent(this, BerandaActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
            finish()
        }

        btnLaporBaru.setOnClickListener {
            startActivity(Intent(this, LaporActivity::class.java))
            finish()
        }
    }
}
