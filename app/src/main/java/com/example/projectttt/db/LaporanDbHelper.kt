package com.example.projectttt.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.projectttt.model.Laporan

class LaporanDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "amankampus.db"
        const val DATABASE_VERSION = 1
        const val TABLE_LAPORAN = "laporan"
        const val COL_ID = "id"
        const val COL_NOMOR_ID = "nomor_id"
        const val COL_KATEGORI = "kategori"
        const val COL_KRONOLOGI = "kronologi"
        const val COL_IS_ANONIM = "is_anonim"
        const val COL_STATUS = "status"
        const val COL_TANGGAL = "tanggal"
        const val COL_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE_LAPORAN (
                $COL_ID       INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMOR_ID TEXT,
                $COL_KATEGORI TEXT,
                $COL_KRONOLOGI TEXT,
                $COL_IS_ANONIM INTEGER,
                $COL_STATUS   TEXT,
                $COL_TANGGAL  TEXT,
                $COL_TIMESTAMP INTEGER
            )
        """.trimIndent()
        db.execSQL(sql)
        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LAPORAN")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        val now = System.currentTimeMillis()
        listOf(
            ContentValues().apply {
                put(COL_NOMOR_ID, "#LAP-2026-0142")
                put(COL_KATEGORI, "Pelecehan Verbal")
                put(COL_KRONOLOGI, "Saya mengalami pelecehan verbal di area parkir kampus pada sore hari.")
                put(COL_IS_ANONIM, 0)
                put(COL_STATUS, "Sedang Diproses")
                put(COL_TANGGAL, "22 APRIL 2026")
                put(COL_TIMESTAMP, now - 86_400_000L)
            },
            ContentValues().apply {
                put(COL_NOMOR_ID, "#LAP-2026-0141")
                put(COL_KATEGORI, "Cyber Bullying")
                put(COL_KRONOLOGI, "Saya mendapat ancaman dan perundungan melalui media sosial dari seseorang di kampus.")
                put(COL_IS_ANONIM, 1)
                put(COL_STATUS, "Terkirim")
                put(COL_TANGGAL, "17 APRIL 2026")
                put(COL_TIMESTAMP, now - 172_800_000L)
            },
            ContentValues().apply {
                put(COL_NOMOR_ID, "#LAP-2026-0140")
                put(COL_KATEGORI, "Konseling Selesai")
                put(COL_KRONOLOGI, "Laporan telah ditangani. Sesi konseling dan tindak lanjut sudah selesai dilakukan.")
                put(COL_IS_ANONIM, 0)
                put(COL_STATUS, "Selesai")
                put(COL_TANGGAL, "17 APRIL 2026")
                put(COL_TIMESTAMP, now - 259_200_000L)
            }
        ).forEach { db.insert(TABLE_LAPORAN, null, it) }
    }

    // ── CREATE ────────────────────────────────────────────────────────────
    fun insertLaporan(laporan: Laporan): Long {
        val values = ContentValues().apply {
            put(COL_NOMOR_ID, laporan.nomorId)
            put(COL_KATEGORI, laporan.kategori)
            put(COL_KRONOLOGI, laporan.kronologi)
            put(COL_IS_ANONIM, if (laporan.isAnonim) 1 else 0)
            put(COL_STATUS, laporan.status)
            put(COL_TANGGAL, laporan.tanggal)
            put(COL_TIMESTAMP, laporan.timestamp)
        }
        return writableDatabase.insert(TABLE_LAPORAN, null, values)
    }

    // ── READ ──────────────────────────────────────────────────────────────
    fun getAllLaporan(): MutableList<Laporan> {
        val list = mutableListOf<Laporan>()
        val cursor = readableDatabase.query(
            TABLE_LAPORAN, null, null, null, null, null, "$COL_TIMESTAMP DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list += Laporan(
                    id        = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                    nomorId   = it.getString(it.getColumnIndexOrThrow(COL_NOMOR_ID)),
                    kategori  = it.getString(it.getColumnIndexOrThrow(COL_KATEGORI)),
                    kronologi = it.getString(it.getColumnIndexOrThrow(COL_KRONOLOGI)),
                    isAnonim  = it.getInt(it.getColumnIndexOrThrow(COL_IS_ANONIM)) == 1,
                    status    = it.getString(it.getColumnIndexOrThrow(COL_STATUS)),
                    tanggal   = it.getString(it.getColumnIndexOrThrow(COL_TANGGAL)),
                    timestamp = it.getLong(it.getColumnIndexOrThrow(COL_TIMESTAMP))
                )
            }
        }
        return list
    }

    // ── UPDATE ────────────────────────────────────────────────────────────
    fun updateLaporan(laporan: Laporan): Int {
        val values = ContentValues().apply {
            put(COL_KATEGORI, laporan.kategori)
            put(COL_KRONOLOGI, laporan.kronologi)
            put(COL_STATUS, laporan.status)
        }
        return writableDatabase.update(
            TABLE_LAPORAN, values, "$COL_ID = ?", arrayOf(laporan.id.toString())
        )
    }

    fun updateStatus(id: Long, newStatus: String): Int {
        val values = ContentValues().apply { put(COL_STATUS, newStatus) }
        return writableDatabase.update(
            TABLE_LAPORAN, values, "$COL_ID = ?", arrayOf(id.toString())
        )
    }

    // ── DELETE ────────────────────────────────────────────────────────────
    fun deleteLaporan(id: Long): Int =
        writableDatabase.delete(TABLE_LAPORAN, "$COL_ID = ?", arrayOf(id.toString()))
}
