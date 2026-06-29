package com.example.projectttt.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectttt.R
import com.example.projectttt.model.Laporan

class LaporanAdapter(
    private var list: MutableList<Laporan>,
    private var layoutMode: Int = MODE_CARD,
    private val onDelete: (Laporan) -> Unit,
    private val onEdit: (Laporan) -> Unit,
    private val onDetail: (Laporan) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MODE_LIST = 0
        const val MODE_GRID = 1
        const val MODE_CARD = 2
    }

    private val expandedPositions = mutableSetOf(0) // first item expanded by default

    fun updateMode(mode: Int) {
        layoutMode = mode
        notifyDataSetChanged()
    }

    fun updateData(newList: MutableList<Laporan>) {
        list = newList
        expandedPositions.clear()
        if (newList.isNotEmpty()) expandedPositions.add(0)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = layoutMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            MODE_LIST -> ListVH(inf.inflate(R.layout.item_laporan_list, parent, false))
            MODE_GRID -> GridVH(inf.inflate(R.layout.item_laporan_grid, parent, false))
            else      -> CardVH(inf.inflate(R.layout.item_laporan_card_rv, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val laporan = list[position]
        when (holder) {
            is ListVH -> holder.bind(laporan)
            is GridVH -> holder.bind(laporan)
            is CardVH -> holder.bind(laporan, position)
        }
    }

    override fun getItemCount(): Int = list.size

    // ── List ViewHolder ──────────────────────────────────────────────────
    inner class ListVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvKategori : TextView  = view.findViewById(R.id.tvKategoriList)
        private val tvNomorId  : TextView  = view.findViewById(R.id.tvNomorIdList)
        private val tvTanggal  : TextView  = view.findViewById(R.id.tvTanggalList)
        private val tvStatus   : TextView  = view.findViewById(R.id.tvStatusList)
        private val btnHapus   : ImageView = view.findViewById(R.id.btnHapusList)

        fun bind(laporan: Laporan) {
            tvKategori.text = laporan.kategori
            tvNomorId.text  = laporan.nomorId
            tvTanggal.text  = laporan.tanggal
            tvStatus.text   = laporan.status
            applyStatusBadge(tvStatus, laporan.status)
            btnHapus.setOnClickListener { onDelete(laporan) }
            itemView.setOnClickListener { onDetail(laporan) }
        }
    }

    // ── Grid ViewHolder ──────────────────────────────────────────────────
    inner class GridVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvKategori : TextView = view.findViewById(R.id.tvKategoriGrid)
        private val tvStatus   : TextView = view.findViewById(R.id.tvStatusGrid)
        private val tvTanggal  : TextView = view.findViewById(R.id.tvTanggalGrid)

        fun bind(laporan: Laporan) {
            tvKategori.text = laporan.kategori
            tvStatus.text   = laporan.status
            tvTanggal.text  = laporan.tanggal
            applyStatusBadge(tvStatus, laporan.status)
            itemView.setOnClickListener { onDetail(laporan) }
        }
    }

    // ── Card ViewHolder ──────────────────────────────────────────────────
    inner class CardVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tvKategori     : TextView     = view.findViewById(R.id.tvKategoriCard)
        private val tvNomorId      : TextView     = view.findViewById(R.id.tvNomorIdCard)
        private val tvTanggal      : TextView     = view.findViewById(R.id.tvTanggalCard)
        private val tvStatus       : TextView     = view.findViewById(R.id.tvStatusCard)
        private val ivIcon         : ImageView    = view.findViewById(R.id.ivIconCard)
        private val ivChevron      : ImageView    = view.findViewById(R.id.ivChevronCard)
        private val layoutTimeline : LinearLayout = view.findViewById(R.id.layoutTimelineCard)
        private val btnEdit        : Button       = view.findViewById(R.id.btnEditCard)
        private val btnHapus       : Button       = view.findViewById(R.id.btnHapusCard)
        private val btnDetail      : Button       = view.findViewById(R.id.btnDetailCard)
        
        // Timeline components
        private val ivStep1 : ImageView = view.findViewById(R.id.ivStep1)
        private val tvStep1 : TextView  = view.findViewById(R.id.tvStep1)
        private val ivStep2 : ImageView = view.findViewById(R.id.ivStep2)
        private val tvStep2 : TextView  = view.findViewById(R.id.tvStep2)
        private val ivStep3 : ImageView = view.findViewById(R.id.ivStep3)
        private val tvStep3 : TextView  = view.findViewById(R.id.tvStep3)
        private val ivStep4 : ImageView = view.findViewById(R.id.ivStep4)
        private val tvStep4 : TextView  = view.findViewById(R.id.tvStep4)

        fun bind(laporan: Laporan, position: Int) {
            tvKategori.text = laporan.kategori
            tvNomorId.text  = laporan.nomorId
            tvTanggal.text  = laporan.tanggal
            tvStatus.text   = laporan.status
            applyStatusBadge(tvStatus, laporan.status)

            val frameIconBg = itemView.findViewById<View>(R.id.frameIconBg)

            // Icon & Background Tint by status
            when (laporan.status) {
                "Selesai" -> {
                    ivIcon.setImageResource(R.drawable.ic_selesai)
                    ivIcon.clearColorFilter()
                    frameIconBg?.setBackgroundResource(R.drawable.bg_icon_green)
                }
                "Terkirim" -> {
                    ivIcon.setImageResource(R.drawable.mail)
                    ivIcon.clearColorFilter()
                    frameIconBg?.setBackgroundResource(R.drawable.bg_icon_blue)
                }
                else -> { // Sedang Diproses
                    ivIcon.setImageResource(R.drawable.ic_proses)
                    ivIcon.clearColorFilter()
                    frameIconBg?.setBackgroundResource(R.drawable.bg_icon_yellow)
                }
            }

            // Timeline states
            // Step 1: Laporan Diterima (always green/active for all statuses)
            ivStep1.setImageResource(R.drawable.ic_check_circle)
            ivStep1.setColorFilter(Color.parseColor("#16A34A"))
            tvStep1.setTextColor(Color.parseColor("#1D293D"))

            // Step 2: Investigasi Awal
            if (laporan.status == "Sedang Diproses" || laporan.status == "Selesai") {
                ivStep2.setImageResource(R.drawable.ic_check_circle)
                ivStep2.setColorFilter(Color.parseColor("#16A34A"))
                tvStep2.setTextColor(Color.parseColor("#1D293D"))
            } else {
                ivStep2.setImageResource(R.drawable.bg_circle_status)
                ivStep2.setColorFilter(Color.parseColor("#CBD5E1"))
                tvStep2.setTextColor(Color.parseColor("#94A3B8"))
            }

            // Step 3: Koordinasi Tim
            if (laporan.status == "Selesai") {
                ivStep3.setImageResource(R.drawable.ic_check_circle)
                ivStep3.setColorFilter(Color.parseColor("#16A34A"))
                tvStep3.setTextColor(Color.parseColor("#1D293D"))
            } else {
                ivStep3.setImageResource(R.drawable.bg_circle_status)
                ivStep3.setColorFilter(Color.parseColor("#CBD5E1"))
                tvStep3.setTextColor(Color.parseColor("#94A3B8"))
            }

            // Step 4: Tindak Lanjut
            if (laporan.status == "Selesai") {
                ivStep4.setImageResource(R.drawable.ic_check_circle)
                ivStep4.setColorFilter(Color.parseColor("#16A34A"))
                tvStep4.setTextColor(Color.parseColor("#1D293D"))
            } else {
                ivStep4.setImageResource(R.drawable.bg_circle_status)
                ivStep4.setColorFilter(Color.parseColor("#CBD5E1"))
                tvStep4.setTextColor(Color.parseColor("#94A3B8"))
            }

            // Show/hide action buttons
            val selesai = laporan.status == "Selesai"
            btnEdit.visibility   = if (selesai) View.GONE else View.VISIBLE
            btnHapus.visibility  = if (selesai) View.GONE else View.VISIBLE
            btnDetail.visibility = View.VISIBLE

            btnEdit.setOnClickListener   { onEdit(laporan) }
            btnHapus.setOnClickListener  { onDelete(laporan) }
            btnDetail.setOnClickListener { onDetail(laporan) }

            // Expand / collapse
            fun updateExpand() {
                val expanded = position in expandedPositions
                layoutTimeline.visibility = if (expanded) View.VISIBLE else View.GONE
                ivChevron.animate().rotation(if (expanded) 180f else 0f).setDuration(250).start()
            }
            updateExpand()

            val toggle = View.OnClickListener {
                if (position in expandedPositions) expandedPositions.remove(position)
                else expandedPositions.add(position)
                updateExpand()
            }
            ivChevron.setOnClickListener(toggle)
            itemView.setOnClickListener(toggle)
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────
    private fun applyStatusBadge(tv: TextView, status: String) {
        val (bg, fg) = when (status) {
            "Sedang Diproses" -> "#FFF3CD" to "#856404"
            "Selesai"         -> "#D1FAE5" to "#065F46"
            "Terkirim"        -> "#DBEAFE" to "#1E40AF"
            else              -> "#F1F5F9" to "#475569"
        }
        tv.setBackgroundColor(Color.parseColor(bg))
        tv.setTextColor(Color.parseColor(fg))
    }
}
