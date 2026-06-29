package com.example.projectttt

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectttt.adapter.EdukasiAdapter
import com.example.projectttt.model.KontenEdukasi

class EdukasiActivity : AppCompatActivity() {

    private lateinit var trendingAdapter : EdukasiAdapter
    private lateinit var allAdapter      : EdukasiAdapter
    private val allKonten = mutableListOf<KontenEdukasi>()
    private var currentFilter = "Semua"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edukasi)

        findViewById<ImageView>(R.id.btnBackEdukasi).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.btnMenuEdukasi)?.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("List View")
            popup.menu.add("Grid View")
            popup.menu.add("Card View")
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    "List View" -> { switchMode(EdukasiAdapter.MODE_LIST); true }
                    "Grid View" -> { switchMode(EdukasiAdapter.MODE_GRID); true }
                    "Card View" -> { switchMode(EdukasiAdapter.MODE_CARD); true }
                    else -> false
                }
            }
            popup.show()
        }

        setupKonten()
        setupRecyclerViews()
        setupChips()
        setupSearch()
    }

    private fun switchMode(mode: Int) {
        trendingAdapter.updateMode(mode)
        allAdapter.updateMode(mode)

        val rvTrending = findViewById<RecyclerView>(R.id.rvTrending)
        val rvSemua = findViewById<RecyclerView>(R.id.rvSemuaKonten)

        if (mode == EdukasiAdapter.MODE_GRID) {
            rvTrending.layoutManager = GridLayoutManager(this, 2)
            rvSemua.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvTrending.layoutManager = LinearLayoutManager(this)
            rvSemua.layoutManager = LinearLayoutManager(this)
        }
    }

    // ── Data ──────────────────────────────────────────────────────────────
    private fun setupKonten() {
        allKonten.addAll(listOf(
            KontenEdukasi(1,
                "Mengenali Tanda-Tanda Pelecehan Seksual",
                "Jangan panik. Kenali Tanda-Tanda dari pelecehan seksual berikut.",
                "Artikel", "5 menit", "Pencegahan", true, "#4F46E5", "article", "img_stop_sa"),
            KontenEdukasi(2,
                "Harus Apa Jika Dilakukan Mengalami Pelecehan?",
                "Jangan panik. Kenali langkah tepat saat Anda mengalami pelecehan.",
                "Video", "8 menit", "Penanganan", true, "#EF4444", "video", "img_speak_out"),
            KontenEdukasi(3,
                "Konseling dan Dukungan Psikologis",
                "Layanan konseling tersedia untuk mendukung korban.",
                "Video", "6 menit", "Penanganan", false, "#EC4899", "video", "img_violence"),
            KontenEdukasi(4,
                "Hak Korban dalam Proses Hukum",
                "Pahami hak-hak Anda sebagai korban kekerasan di kampus.",
                "Video", "12 menit", "Penanganan", false, "#8B5CF6", "audio", "img_degrees_and_penalties_of_sa"),
            KontenEdukasi(5,
                "Membangun Kampus yang Aman",
                "Bersama-sama ciptakan lingkungan kampus bebas kekerasan.",
                "Artikel", "5 menit", "Pencegahan", false, "#10B981", "article", "img_recent_incident"),
            KontenEdukasi(6,
                "Cara Melaporkan Kejadian dengan Aman",
                "Panduan langkah demi langkah untuk melapor secara aman.",
                "Video", "10 menit", "Penanganan", false, "#F97316", "video", "img_no_means")
        ))
    }

    // ── RecyclerViews ─────────────────────────────────────────────────────
    private fun setupRecyclerViews() {
        // Trending RecyclerView
        val rvTrending = findViewById<RecyclerView>(R.id.rvTrending)
        trendingAdapter = EdukasiAdapter(allKonten.filter { it.isTrending }) { onKontenClick(it) }
        rvTrending.layoutManager = LinearLayoutManager(this)
        rvTrending.adapter       = trendingAdapter
        rvTrending.isNestedScrollingEnabled = false

        // All content RecyclerView
        val rvSemua = findViewById<RecyclerView>(R.id.rvSemuaKonten)
        allAdapter = EdukasiAdapter(allKonten) { onKontenClick(it) }
        rvSemua.layoutManager = LinearLayoutManager(this)
        rvSemua.adapter       = allAdapter
        rvSemua.isNestedScrollingEnabled = false
    }

    private fun onKontenClick(k: KontenEdukasi) {
        Toast.makeText(this, k.judul, Toast.LENGTH_SHORT).show()
    }

    // ── Filter chips ──────────────────────────────────────────────────────
    private fun setupChips() {
        val chipSemua      = findViewById<LinearLayout>(R.id.chipSemua)
        val chipPencegahan = findViewById<LinearLayout>(R.id.chipPencegahan)
        val chipPenanganan = findViewById<LinearLayout>(R.id.chipPenanganan)

        fun activate(chip: LinearLayout) {
            chip.setBackgroundResource(R.drawable.bg_chip_active)
            (chip.getChildAt(1) as? TextView)?.setTextColor(Color.WHITE)
            (chip.getChildAt(0) as? ImageView)?.setColorFilter(Color.WHITE)
        }
        fun deactivate(chip: LinearLayout) {
            chip.setBackgroundResource(R.drawable.bg_chip_inactive)
            (chip.getChildAt(1) as? TextView)?.setTextColor(Color.parseColor("#1C398E"))
            (chip.getChildAt(0) as? ImageView)?.setColorFilter(Color.parseColor("#1C398E"))
        }

        chipSemua.setOnClickListener {
            currentFilter = "Semua"
            activate(chipSemua); deactivate(chipPencegahan); deactivate(chipPenanganan)
            filterKonten("")
        }
        chipPencegahan.setOnClickListener {
            currentFilter = "Pencegahan"
            activate(chipPencegahan); deactivate(chipSemua); deactivate(chipPenanganan)
            filterKonten("")
        }
        chipPenanganan.setOnClickListener {
            currentFilter = "Penanganan"
            activate(chipPenanganan); deactivate(chipSemua); deactivate(chipPencegahan)
            filterKonten("")
        }
    }

    // ── Search ────────────────────────────────────────────────────────────
    private fun setupSearch() {
        val etSearch = findViewById<EditText>(R.id.etCariEdukasi)
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) { filterKonten(s.toString()) }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterKonten(query: String) {
        val filtered = allKonten.filter { k ->
            val matchFilter = currentFilter == "Semua" || k.kategori == currentFilter
            val matchQuery  = query.isEmpty() || k.judul.contains(query, ignoreCase = true)
            matchFilter && matchQuery
        }
        trendingAdapter.updateData(filtered.filter { it.isTrending })
        allAdapter.updateData(filtered)
    }
}
