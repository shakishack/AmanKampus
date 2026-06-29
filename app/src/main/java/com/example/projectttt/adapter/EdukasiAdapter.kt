package com.example.projectttt.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectttt.R
import com.example.projectttt.model.KontenEdukasi

class EdukasiAdapter(
    private var list: List<KontenEdukasi>,
    private var layoutMode: Int = MODE_LIST,
    private val onClick: (KontenEdukasi) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val MODE_LIST = 0
        const val MODE_GRID = 1
        const val MODE_CARD = 2
    }

    fun updateMode(mode: Int) {
        layoutMode = mode
        notifyDataSetChanged()
    }

    fun updateData(newList: List<KontenEdukasi>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = layoutMode

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            MODE_LIST -> ListVH(inf.inflate(R.layout.item_edukasi_list, parent, false))
            MODE_GRID -> GridVH(inf.inflate(R.layout.item_edukasi_grid, parent, false))
            else      -> CardVH(inf.inflate(R.layout.item_edukasi_card, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val konten = list[position]
        when (holder) {
            is ListVH -> holder.bind(konten)
            is GridVH -> holder.bind(konten)
            is CardVH -> holder.bind(konten)
        }
    }

    override fun getItemCount(): Int = list.size

    // ── List ViewHolder ──
    inner class ListVH(view: View) : RecyclerView.ViewHolder(view) {
        private val ivCover : ImageView = view.findViewById(R.id.ivEdukasiCoverList)
        private val tvJudul : TextView  = view.findViewById(R.id.tvJudulEdukasiList)
        private val tvTipe  : TextView  = view.findViewById(R.id.tvTipeEdukasiList)
        private val tvDurasi: TextView  = view.findViewById(R.id.tvDurasiEdukasiList)

        fun bind(k: KontenEdukasi) {
            tvJudul.text  = k.judul
            tvTipe.text   = k.tipe
            tvDurasi.text = "⏱ ${k.durasi}"

            val context = itemView.context
            val imageResId = context.resources.getIdentifier(k.imageResName, "drawable", context.packageName)
            if (imageResId != 0) {
                ivCover.setImageResource(imageResId)
            } else {
                ivCover.setImageResource(R.drawable.img_stop_sa)
            }
            itemView.setOnClickListener { onClick(k) }
        }
    }

    // ── Grid ViewHolder ──
    inner class GridVH(view: View) : RecyclerView.ViewHolder(view) {
        private val ivCover : ImageView = view.findViewById(R.id.ivEdukasiCoverGrid)
        private val tvDurasi: TextView  = view.findViewById(R.id.tvDurasiEdukasiGrid)
        private val tvTipe  : TextView  = view.findViewById(R.id.tvTipeEdukasiGrid)
        private val tvJudul : TextView  = view.findViewById(R.id.tvJudulEdukasiGrid)

        fun bind(k: KontenEdukasi) {
            tvJudul.text  = k.judul
            tvTipe.text   = k.tipe
            tvDurasi.text = k.durasi

            val context = itemView.context
            val imageResId = context.resources.getIdentifier(k.imageResName, "drawable", context.packageName)
            if (imageResId != 0) {
                ivCover.setImageResource(imageResId)
            } else {
                ivCover.setImageResource(R.drawable.img_stop_sa)
            }
            itemView.setOnClickListener { onClick(k) }
        }
    }

    // ── Card ViewHolder ──
    inner class CardVH(view: View) : RecyclerView.ViewHolder(view) {
        private val ivCover    : ImageView = view.findViewById(R.id.ivEdukasiCover)
        private val tvDurasi   : TextView  = view.findViewById(R.id.tvDurasiEdukasi)
        private val tvTipe     : TextView  = view.findViewById(R.id.tvTipeEdukasi)
        private val tvJudul    : TextView  = view.findViewById(R.id.tvJudulEdukasi)
        private val tvDeskripsi: TextView  = view.findViewById(R.id.tvDeskripsiEdukasi)
        private val tvAction   : TextView  = view.findViewById(R.id.tvEdukasiAction)

        fun bind(k: KontenEdukasi) {
            tvJudul.text     = k.judul
            tvDeskripsi.text = k.deskripsi
            tvTipe.text      = k.tipe
            tvDurasi.text    = k.durasi

            tvAction.text = if (k.tipe == "Video") "Nonton Selengkapnya →" else "Baca Selengkapnya →"

            val context = itemView.context
            val imageResId = context.resources.getIdentifier(k.imageResName, "drawable", context.packageName)
            if (imageResId != 0) {
                ivCover.setImageResource(imageResId)
            } else {
                ivCover.setImageResource(R.drawable.img_stop_sa)
            }
            itemView.setOnClickListener { onClick(k) }
        }
    }
}
