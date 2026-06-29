package com.example.projectttt.model

data class Laporan(
    val id: Long = 0,
    val nomorId: String = "",
    val kategori: String = "",
    val kronologi: String = "",
    val isAnonim: Boolean = false,
    var status: String = "Terkirim",
    val tanggal: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
