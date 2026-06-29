package com.example.projectttt

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectttt.adapter.LaporanAdapter
import com.example.projectttt.db.LaporanDbHelper
import com.example.projectttt.model.Laporan

class StatusActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter      : LaporanAdapter
    private lateinit var dbHelper     : LaporanDbHelper
    private var currentMode = LaporanAdapter.MODE_CARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        findViewById<ImageView>(R.id.btnBackStatus).setOnClickListener { finish() }
        val btnMenuStatus = findViewById<ImageView>(R.id.btnMenuStatus)
        btnMenuStatus.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("List View")
            popup.menu.add("Grid View")
            popup.menu.add("Card View")
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    "List View" -> switchMode(LaporanAdapter.MODE_LIST)
                    "Grid View" -> switchMode(LaporanAdapter.MODE_GRID)
                    "Card View" -> switchMode(LaporanAdapter.MODE_CARD)
                    else -> false
                }
            }
            popup.show()
        }

        dbHelper     = LaporanDbHelper(this)
        recyclerView = findViewById(R.id.recyclerViewLaporan)

        loadData()

        findViewById<Button>(R.id.btnBuatLaporanBaru)?.setOnClickListener {
            startActivity(Intent(this, LaporActivity::class.java))
        }
    }

    private fun loadData() {
        val laporan = dbHelper.getAllLaporan()

        adapter = LaporanAdapter(
            list      = laporan,
            layoutMode= currentMode,
            onDelete  = { item -> showDeleteDialog(item) },
            onEdit    = { item -> showEditDialog(item) },
            onDetail  = { item -> showDetail(item) }
        )

        recyclerView.adapter = adapter
        applyLayoutManager(currentMode)

        updateSummary(laporan)

        val emptyView    = findViewById<View>(R.id.emptyStateLayout)
        val footerStatus = findViewById<View>(R.id.footerStatus)
        val btnBuatEmpty = findViewById<Button>(R.id.btnBuatLaporanBaruEmpty)

        btnBuatEmpty?.setOnClickListener {
            val intent = Intent(this, LaporActivity::class.java)
            startActivity(intent)
        }

        if (laporan.isEmpty()) {
            emptyView?.visibility  = View.VISIBLE
            recyclerView.visibility= View.GONE
            footerStatus?.visibility = View.GONE
        } else {
            emptyView?.visibility  = View.GONE
            recyclerView.visibility= View.VISIBLE
            footerStatus?.visibility = View.VISIBLE
        }
    }

    private fun applyLayoutManager(mode: Int) {
        recyclerView.layoutManager = when (mode) {
            LaporanAdapter.MODE_GRID -> GridLayoutManager(this, 2)
            else                     -> LinearLayoutManager(this)
        }
    }

    private fun updateSummary(laporan: List<Laporan>) {
        val diproses = laporan.count { it.status == "Sedang Diproses" || it.status == "Terkirim" }
        val selesai  = laporan.count { it.status == "Selesai" }
        findViewById<TextView>(R.id.tvTotalLaporan)?.text  = "${laporan.size} Laporan Total"
        findViewById<TextView>(R.id.tvDiprosesCount)?.text = "$diproses Diproses"
        findViewById<TextView>(R.id.tvSelesaiCount)?.text  = "$selesai Selesai"
    }

    private fun showDeleteDialog(laporan: Laporan) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Laporan")
            .setMessage("Yakin ingin menghapus laporan ${laporan.nomorId}?")
            .setPositiveButton("Hapus") { _, _ ->
                dbHelper.deleteLaporan(laporan.id)
                loadData()
                Toast.makeText(this, "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(laporan: Laporan) {
        val dialogView  = layoutInflater.inflate(R.layout.dialog_edit_laporan, null)
        val etKronologi = dialogView.findViewById<EditText>(R.id.etEditKronologi)
        etKronologi.setText(laporan.kronologi)

        AlertDialog.Builder(this)
            .setTitle("Edit Laporan ${laporan.nomorId}")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val updated = laporan.copy(kronologi = etKronologi.text.toString().trim())
                dbHelper.updateLaporan(updated)
                loadData()
                Toast.makeText(this, "Laporan berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDetail(laporan: Laporan) {
        AlertDialog.Builder(this)
            .setTitle("${laporan.kategori}  —  ${laporan.status}")
            .setMessage(
                "ID: ${laporan.nomorId}\n" +
                "Tanggal: ${laporan.tanggal}\n" +
                "Anonim: ${if (laporan.isAnonim) "Ya" else "Tidak"}\n\n" +
                "Kronologi:\n${laporan.kronologi}"
            )
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun switchMode(mode: Int): Boolean {
        currentMode = mode
        adapter.updateMode(currentMode)
        applyLayoutManager(currentMode)
        return true
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) loadData()
    }
}
