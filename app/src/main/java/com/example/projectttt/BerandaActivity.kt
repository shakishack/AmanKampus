package com.example.projectttt

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.projectttt.db.LaporanDbHelper
import com.example.projectttt.receiver.NotifikasiReceiver

class BerandaActivity : AppCompatActivity() {

    private val sosHandler   = Handler(Looper.getMainLooper())
    private var isSosPressed = false
    private val SOS_THRESHOLD = 3_000L

    private var sosAnimator: android.animation.AnimatorSet? = null
    private var isSosActive = false

    private fun startSosAnimation(btnSos: ImageView) {
        val scaleX = android.animation.ObjectAnimator.ofFloat(btnSos, "scaleX", 1.0f, 1.08f, 1.0f).apply {
            repeatCount = android.animation.ValueAnimator.INFINITE
            duration = 1000
        }
        val scaleY = android.animation.ObjectAnimator.ofFloat(btnSos, "scaleY", 1.0f, 1.08f, 1.0f).apply {
            repeatCount = android.animation.ValueAnimator.INFINITE
            duration = 1000
        }
        sosAnimator = android.animation.AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun stopSosAnimation(btnSos: ImageView) {
        sosAnimator?.cancel()
        sosAnimator = null
        btnSos.scaleX = 1.0f
        btnSos.scaleY = 1.0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        val btnSos     = findViewById<ImageView>(R.id.btnSos)
        val tvSosStatus= findViewById<TextView>(R.id.tvSosStatus)
        val menuLapor  = findViewById<CardView>(R.id.menuLapor)
        val menuStatus = findViewById<CardView>(R.id.menuStatus)
        val menuEdukasi= findViewById<CardView>(R.id.menuEdukasi)

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val prefs = getSharedPreferences("AmanKampusPrefs", MODE_PRIVATE)
        val username = prefs.getString("USER_NAME", "Andrew")
        tvGreeting.text = "Halo, $username!"

        btnSos.setOnTouchListener { _, event ->
            if (isSosActive) {
                if (event.action == MotionEvent.ACTION_UP) {
                    isSosActive = false
                    stopSosAnimation(btnSos)
                    tvSosStatus.text = "Tahan 3 detik untuk aktivasi"
                    tvSosStatus.setTextColor(Color.parseColor("#64748B"))
                    Toast.makeText(this, "Panggilan Darurat dinonaktifkan.", Toast.LENGTH_SHORT).show()
                }
                return@setOnTouchListener true
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isSosPressed = true
                    tvSosStatus.text = "Lepas untuk membatalkan..."
                    tvSosStatus.setTextColor(Color.RED)
                    sosHandler.postDelayed(sosRunnable, SOS_THRESHOLD)
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (isSosPressed) {
                        isSosPressed = false
                        sosHandler.removeCallbacks(sosRunnable)
                        tvSosStatus.text = "Tahan 3 detik untuk aktivasi"
                        tvSosStatus.setTextColor(Color.parseColor("#64748B"))
                    }
                    true
                }
                else -> false
            }
        }

        menuLapor.setOnClickListener   { startActivity(Intent(this, LaporActivity::class.java)) }
        menuStatus.setOnClickListener  { startActivity(Intent(this, StatusActivity::class.java)) }
        menuEdukasi.setOnClickListener { startActivity(Intent(this, EdukasiActivity::class.java)) }
        findViewById<CardView>(R.id.menuFaq).setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }

        findViewById<CardView>(R.id.menuLaporCard).setOnClickListener   { startActivity(Intent(this, LaporActivity::class.java)) }
        findViewById<CardView>(R.id.menuStatusCard).setOnClickListener  { startActivity(Intent(this, StatusActivity::class.java)) }
        findViewById<CardView>(R.id.menuEdukasiCard).setOnClickListener { startActivity(Intent(this, EdukasiActivity::class.java)) }
        findViewById<CardView>(R.id.menuFaqCard).setOnClickListener     { startActivity(Intent(this, FaqActivity::class.java)) }

        val layoutGrid = findViewById<android.widget.LinearLayout>(R.id.layoutMenuGrid)
        val layoutCard = findViewById<android.widget.LinearLayout>(R.id.layoutMenuCard)

        findViewById<ImageView>(R.id.btnMenuBeranda).setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("Grid View")
            popup.menu.add("List View")
            
            val logoutItem = popup.menu.add("Logout")
            val spannable = android.text.SpannableString("Logout")
            spannable.setSpan(
                android.text.style.ForegroundColorSpan(android.graphics.Color.RED),
                0, spannable.length, android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            logoutItem.title = spannable

            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title.toString()) {
                    "Grid View" -> {
                        layoutGrid.visibility = android.view.View.VISIBLE
                        layoutCard.visibility = android.view.View.GONE
                        prefs.edit().putString("MENU_MODE", "GRID").apply()
                        true
                    }
                    "List View" -> {
                        layoutGrid.visibility = android.view.View.GONE
                        layoutCard.visibility = android.view.View.VISIBLE
                        prefs.edit().putString("MENU_MODE", "CARD").apply()
                        true
                    }
                    "Logout" -> {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        val savedMode = prefs.getString("MENU_MODE", "GRID")
        if (savedMode == "CARD") {
            layoutGrid.visibility = android.view.View.GONE
            layoutCard.visibility = android.view.View.VISIBLE
        } else {
            layoutGrid.visibility = android.view.View.VISIBLE
            layoutCard.visibility = android.view.View.GONE
        }

        findViewById<ImageView>(R.id.ivNotif).setOnClickListener {
            showNotificationDialog()
        }
    }

    private fun showNotificationDialog() {
        val laporanList = LaporanDbHelper(this).getAllLaporan()
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Notifikasi Terkini 🔔")

        val items = mutableListOf<String>()
        items.add("📢 Informasi: Layanan bantuan aktif 24 jam.")
        if (isSosActive) {
            items.add("🚨 SOS: Panggilan Darurat Anda sedang aktif!")
        }

        if (laporanList.isNotEmpty()) {
            for (laporan in laporanList.take(3)) {
                items.add("📝 ${laporan.nomorId} (${laporan.kategori}): Status ${laporan.status}")
            }
        } else {
            items.add("📭 Belum ada laporan terkirim.")
        }

        builder.setItems(items.toTypedArray(), null)
        builder.setPositiveButton("Tutup", null)
        builder.show()
    }

    private val sosRunnable = Runnable {
        if (isSosPressed) showSosActivatedDialog()
    }

    private fun showSosActivatedDialog() {
        val btnSos = findViewById<ImageView>(R.id.btnSos)
        val tvSosStatus = findViewById<TextView>(R.id.tvSosStatus)
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_sos_activated)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(0.7f)
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        dialog.findViewById<Button>(R.id.btnLanjutkan).setOnClickListener {
            dialog.dismiss()
            NotifikasiReceiver.sendSosBroadcast(this)
            isSosPressed = false
            isSosActive = true
            startSosAnimation(btnSos)
            tvSosStatus.apply {
                text = "🚨 SOS AKTIF - Tekan untuk Berhenti"
                setTextColor(Color.RED)
            }
        }
        dialog.show()
    }

    private fun updateAktivitasBadge() {
        try {
            val laporan = LaporanDbHelper(this).getAllLaporan()
            val total = laporan.size
            val diproses = laporan.count { it.status == "Sedang Diproses" || it.status == "Terkirim" }

            val tvBadgeStatus = findViewById<TextView>(R.id.tvBadgeStatus)
            tvBadgeStatus?.text = if (diproses > 0) "Sedang Diproses" else "Selesai"

            val tvBadgeMenu = findViewById<TextView>(R.id.tvBadge)
            tvBadgeMenu?.text = total.toString()
            tvBadgeMenu?.visibility = if (total > 0) android.view.View.VISIBLE else android.view.View.GONE

            val tvBadgeCard = findViewById<TextView>(R.id.tvBadgeCard)
            tvBadgeCard?.text = total.toString()
            tvBadgeCard?.visibility = if (total > 0) android.view.View.VISIBLE else android.view.View.GONE
        } catch (_: Exception) { }
    }

    override fun onResume() {
        super.onResume()
        updateAktivitasBadge()
    }
}
