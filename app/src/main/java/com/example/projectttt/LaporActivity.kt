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

    private val pickImageLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            Toast.makeText(this, "Bukti gambar berhasil diunggah!", Toast.LENGTH_SHORT).show()
            val btnUploadImage = findViewById<LinearLayout>(R.id.btnUploadImage)
            btnUploadImage.setBackgroundResource(R.drawable.bg_badge_green)
            btnUploadImage.findViewById<ImageView>(R.id.ivIconImage)?.setColorFilter(
                android.graphics.Color.parseColor("#059669")
            )
            btnUploadImage.findViewById<TextView>(R.id.tvLabelImage)?.apply {
                text = "Gambar Terpilih"
                setTextColor(android.graphics.Color.parseColor("#059669"))
            }
        }
    }

    private val pickAudioLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            Toast.makeText(this, "Bukti audio berhasil diunggah!", Toast.LENGTH_SHORT).show()
            val btnUploadAudio = findViewById<LinearLayout>(R.id.btnUploadAudio)
            btnUploadAudio.setBackgroundResource(R.drawable.bg_badge_green)
            btnUploadAudio.findViewById<ImageView>(R.id.ivIconAudio)?.setColorFilter(
                android.graphics.Color.parseColor("#059669")
            )
            btnUploadAudio.findViewById<TextView>(R.id.tvLabelAudio)?.apply {
                text = "Audio Terpilih"
                setTextColor(android.graphics.Color.parseColor("#059669"))
            }
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(this, "Bukti foto berhasil diunggah!", Toast.LENGTH_SHORT).show()
            val btnUploadCamera = findViewById<LinearLayout>(R.id.btnUploadCamera)
            btnUploadCamera.setBackgroundResource(R.drawable.bg_badge_green)
            btnUploadCamera.findViewById<ImageView>(R.id.ivIconCamera)?.setColorFilter(
                android.graphics.Color.parseColor("#059669")
            )
            btnUploadCamera.findViewById<TextView>(R.id.tvLabelCamera)?.apply {
                text = "Foto Terpilih"
                setTextColor(android.graphics.Color.parseColor("#059669"))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lapor)

        val btnBack            = findViewById<ImageView>(R.id.btnBackLapor)
        val spinnerContainer   = findViewById<RelativeLayout>(R.id.spinnerKategoriContainer)
        val tvSelectedKategori = findViewById<TextView>(R.id.tvSelectedKategori)
        val ivArrowKategori    = findViewById<ImageView>(R.id.ivArrowKategori)
        val etKronologi        = findViewById<EditText>(R.id.etKronologi)
        val switchAnonim       = findViewById<Switch>(R.id.switchAnonim)
        val btnKirim           = findViewById<Button>(R.id.btnKirimLaporan)

        val btnUploadCamera    = findViewById<LinearLayout>(R.id.btnUploadCamera)
        val btnUploadAudio     = findViewById<LinearLayout>(R.id.btnUploadAudio)
        val btnUploadImage     = findViewById<LinearLayout>(R.id.btnUploadImage)

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

        btnUploadCamera.setOnClickListener {
            takePictureLauncher.launch(null)
        }

        btnUploadAudio.setOnClickListener {
            pickAudioLauncher.launch("audio/*")
        }

        btnUploadImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
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
        return "SR-$year-$random"
    }
}
