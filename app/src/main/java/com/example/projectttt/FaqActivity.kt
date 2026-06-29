package com.example.projectttt

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class FaqActivity : AppCompatActivity() {

    private lateinit var faqContainer: LinearLayout
    private lateinit var etSearch: EditText

    private val faqList = listOf(
        FaqItem(
            "Bagaimana cara melaporkan tindak kekerasan secara anonim?",
            "Anda dapat mengaktifkan opsi 'Laporkan Anonim' di bagian atas formulir pelaporan sebelum mengirimkan laporan. Identitas Anda tidak akan disimpan di database pelaporan publik."
        ),
        FaqItem(
            "Berapa lama waktu respon tim keamanan kampus?",
            "Waktu respon darurat biasanya kurang dari 3 menit sejak tombol SOS ditekan. Tim keamanan kampus terdekat akan segera diarahkan menuju koordinat Anda."
        ),
        FaqItem(
            "Apakah lokasi saya akan dilacak saat menekan tombol SOS?",
            "Ya, fitur SOS secara otomatis melacak koordinat GPS perangkat Anda agar tim keamanan dapat mendatangi lokasi Anda secara presisi. Pelacakan akan dinonaktifkan saat Anda mematikan mode darurat."
        ),
        FaqItem(
            "Apa yang harus saya lakukan jika menyaksikan tindak kriminal?",
            "Segera cari tempat yang aman, lalu buat laporan dengan menyertakan kronologi detail serta bukti foto/audio jika memungkinkan, atau tekan tombol SOS jika terjadi keadaan darurat."
        ),
        FaqItem(
            "Siapa saja yang dapat melihat laporan saya?",
            "Hanya petugas administrasi keamanan kampus yang berwenang dan tim investigasi khusus yang dapat mengakses detail laporan Anda demi kerahasiaan dan keamanan."
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        faqContainer = findViewById(R.id.faqContainer)
        etSearch = findViewById(R.id.etCariFaq)

        findViewById<ImageView>(R.id.btnBackFaq).setOnClickListener { finish() }
        findViewById<ImageView>(R.id.btnMenuFaq)?.setOnClickListener { view ->
            val popup = androidx.appcompat.widget.PopupMenu(this, view)
            popup.menu.add("Panduan FAQ")
            popup.menu.add("Kirim Feedback")
            popup.setOnMenuItemClickListener { menuItem ->
                android.widget.Toast.makeText(this, menuItem.title, android.widget.Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

        displayFaqs("")

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                displayFaqs(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun displayFaqs(query: String) {
        faqContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (item in faqList) {
            if (query.isNotEmpty() && !item.pertanyaan.contains(query, ignoreCase = true) && !item.jawaban.contains(query, ignoreCase = true)) {
                continue
            }

            val faqView = inflater.inflate(R.layout.item_faq, faqContainer, false)
            val tvQuestion = faqView.findViewById<TextView>(R.id.tvFaqPertanyaan)
            val tvAnswer = faqView.findViewById<TextView>(R.id.tvFaqJawaban)
            val ivChevron = faqView.findViewById<ImageView>(R.id.ivFaqChevron)
            val layoutHeader = faqView.findViewById<LinearLayout>(R.id.layoutFaqHeader)
            val layoutAnswer = faqView.findViewById<LinearLayout>(R.id.layoutFaqJawaban)

            tvQuestion.text = item.pertanyaan
            tvAnswer.text = item.jawaban

            var isExpanded = false
            layoutHeader.setOnClickListener {
                isExpanded = !isExpanded
                layoutAnswer.visibility = if (isExpanded) View.VISIBLE else View.GONE
                ivChevron.animate().rotation(if (isExpanded) 180f else 0f).setDuration(200).start()
            }

            faqContainer.addView(faqView)
        }
    }

    data class FaqItem(val pertanyaan: String, val jawaban: String)
}
