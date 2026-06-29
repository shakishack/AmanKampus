package com.example.projectttt.model

data class KontenEdukasi(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val tipe: String,       // "Artikel", "Video", "Podcast"
    val durasi: String,
    val kategori: String,   // "Semua", "Pencegahan", "Penanganan"
    val isTrending: Boolean = false,
    val bgColorHex: String,
    val iconType: String,   // "article", "video", "audio"
    val imageResName: String
)
