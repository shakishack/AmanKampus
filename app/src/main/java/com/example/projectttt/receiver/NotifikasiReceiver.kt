package com.example.projectttt.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.projectttt.BerandaActivity
import com.example.projectttt.R
import com.example.projectttt.StatusActivity

class NotifikasiReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_LAPORAN_TERKIRIM = "com.example.projectttt.ACTION_LAPORAN_TERKIRIM"
        const val ACTION_SOS_ACTIVATED    = "com.example.projectttt.ACTION_SOS_ACTIVATED"
        const val EXTRA_NOMOR_LAPORAN     = "extra_nomor_laporan"
        const val CHANNEL_ID              = "amankampus_channel"
        const val CHANNEL_NAME            = "AmanKampus Notifikasi"

        /** Buat channel notifikasi (panggil dari Application / SplashActivity). */
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Notifikasi dari AmanKampus" }
                (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(channel)
            }
        }

        fun sendLaporanTerkirimBroadcast(context: Context, nomorLaporan: String) {
            context.sendBroadcast(
                Intent(ACTION_LAPORAN_TERKIRIM).apply {
                    putExtra(EXTRA_NOMOR_LAPORAN, nomorLaporan)
                    setPackage(context.packageName)
                }
            )
        }

        fun sendSosBroadcast(context: Context) {
            context.sendBroadcast(
                Intent(ACTION_SOS_ACTIVATED).apply { setPackage(context.packageName) }
            )
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)
        when (intent.action) {
            ACTION_LAPORAN_TERKIRIM ->
                showLaporanNotification(context, intent.getStringExtra(EXTRA_NOMOR_LAPORAN) ?: "")
            ACTION_SOS_ACTIVATED ->
                showSosNotification(context)
        }
    }

    // ── Notifikasi Laporan Terkirim ───────────────────────────────────────
    private fun showLaporanNotification(context: Context, nomorLaporan: String) {
        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, StatusActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notif_laporan)
            .setContentTitle("✅ Laporan Berhasil Dikirim")
            .setContentText("Laporan $nomorLaporan telah diterima dan sedang diproses.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Laporan $nomorLaporan telah diterima. Tim kami akan segera menindaklanjuti laporan Anda.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(1, notif)
    }

    // ── Notifikasi SOS ───────────────────────────────────────────────────
    private fun showSosNotification(context: Context) {
        val pendingIntent = PendingIntent.getActivity(
            context, 2,
            Intent(context, BerandaActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_warning)
            .setContentTitle("🚨 SOS Darurat Diaktifkan!")
            .setContentText("Bantuan darurat sedang dalam perjalanan. Tetap tenang.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(2, notif)
    }
}
