package com.example.projectttt

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projectttt.db.LaporanDbHelper
import com.example.projectttt.model.Laporan
import com.example.projectttt.receiver.NotifikasiReceiver
import java.text.SimpleDateFormat
import java.util.*

class LaporActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapor)

        val btnBack            = findViewById<ImageView>(R.id.btnBackLapor)
        val spinnerContainer   = findViewById<android.widget.RelativeLayout>(R.id.spinnerKategoriContainer)
        val tvSelectedKategori = findViewById<TextView>(R.id.tvSelectedKategori)
        val ivArrowKategori    = findViewById<ImageView>(R.id.ivArrowKategori)
        val etKronologi        = findViewById<EditText>(R.id.etKronologi)
        val switchAnonim       = findViewById<Switch>(R.id.switchAnonim)
        val btnKirim           = findViewById<Button>(R.id.btnKirimLaporan)

        btnBack.setOnClickListener { finish() }

        val kategoriList = arrayOf(
            "Pelecehan Seksual",
            "Kekerasan Fisik",
            "Kekerasan Verbal",
            "Stalking",
            "Cyber Bullying",
            "Diskriminasi",
            "Lainnya"
        )

        var selectedKategori = ""

        spinnerContainer.setOnClickListener { view ->
            ivArrowKategori.animate().rotation(180f).setDuration(200).start()
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            for (kategori in kategoriList) {
                popup.menu.add(kategori)
            }
            popup.setOnMenuItemClickListener { menuItem ->
                selectedKategori = menuItem.title.toString()
                tvSelectedKategori.text = selectedKategori
                tvSelectedKategori.setTextColor(android.graphics.Color.parseColor("#1D293D"))
                true
            }
            popup.setOnDismissListener {
                ivArrowKategori.animate().rotation(0f).setDuration(200).start()
            }
            popup.show()
        }

        findViewById<ImageView>(R.id.btnMenuLapor)?.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("Panduan Pelaporan")
            popup.menu.add("Hubungi Hotline")
            popup.setOnMenuItemClickListener { menuItem ->
                Toast.makeText(this, menuItem.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

        btnKirim.setOnClickListener {
            val kronologi = etKronologi.text.toString().trim()
            val isAnonim  = switchAnonim.isChecked

            if (selectedKategori.isEmpty()) {
                Toast.makeText(this, "Pilih kategori terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (kronologi.isEmpty()) {
                Toast.makeText(this, "Isi kronologi kejadian terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nomorId = generateNomorId()
            val tanggal = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                .format(Date()).uppercase(Locale("id", "ID"))

            val laporan = Laporan(
                nomorId   = nomorId,
                kategori  = selectedKategori,
                kronologi = kronologi,
                isAnonim  = isAnonim,
                status    = "Terkirim",
                tanggal   = tanggal,
                timestamp = System.currentTimeMillis()
            )

            LaporanDbHelper(this).insertLaporan(laporan)

            // Broadcast → notifikasi
            NotifikasiReceiver.sendLaporanTerkirimBroadcast(this, nomorId)

            startActivity(
                Intent(this, LaporanTerkirimActivity::class.java)
                    .putExtra("nomor_id", nomorId)
            )
            finish()
        }
    }

    private fun generateNomorId(): String {
        val year   = Calendar.getInstance().get(Calendar.YEAR)
        val random = (1000..9999).random()
        return "#SR-$year-$random"
    }
}
