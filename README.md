# AmanKampus 🛡️

AmanKampus adalah aplikasi mobile berbasis Android yang dirancang khusus untuk meningkatkan keamanan dan kenyamanan di lingkungan kampus. Aplikasi ini memungkinkan mahasiswa dan civitas akademika untuk melaporkan kejadian darurat, melacak status laporan secara real-time, mempelajari panduan edukasi keamanan, serta mengakses bantuan cepat melalui tombol SOS interaktif.

---

## 📱 Daftar Halaman & Fitur Utama

### 1. 🔐 Halaman Login / Daftar Akun
*   **Registrasi Akun:** Memungkinkan pengguna baru untuk mendaftar dengan melengkapi Nama Lengkap, Username, Email, dan Password.
*   **Keamanan Akun (Eye Toggle):** Dilengkapi dengan tombol toggle (ikon mata) interaktif untuk menyembunyikan atau memperlihatkan password saat mengetik guna menjaga kerahasiaan.
*   **Autentikasi & Personalisasi:** Data Nama Lengkap disimpan dengan aman di `SharedPreferences` lokal, sehingga aplikasi dapat menyapa pengguna secara personal setelah masuk (misalnya, *"Halo, Nahda!"*).

### 2. 🏠 Beranda (Dashboard Utama)
*   **Sapaan Dinamis:** Menampilkan ucapan selamat datang personal yang disesuaikan secara real-time berdasarkan profil pengguna yang terdaftar.
*   **Dynamic Badge Count:** Tombol menu *Status Laporan* dilengkapi dengan lencana jumlah total laporan yang berjalan dinamis dan otomatis bertambah seiring masuknya laporan baru.
*   **Lonceng Notifikasi Terkini:** Mengambil data laporan terbaru langsung dari database SQLite untuk memberikan update status real-time kepada pengguna.
*   **Mode Tampilan Menu Fleksibel:** Pengguna dapat beralih antara **Grid View** (tampilan kartu 2x2) dan **List View** (tampilan daftar memanjang) melalui menu opsi 3-titik di kanan atas. Mode layout ini disimpan secara otomatis dan persisten menggunakan SharedPreferences.
*   **Logout Aman:** Menu Logout berwana merah di pojok kanan atas yang membersihkan seluruh tumpukan aktivitas (*clear task*) dan mengarahkan kembali ke halaman login.

### 3. 🚨 SOS Button (Panggilan Darurat Cepat)
*   **Aktivasi Tahan 3 Detik:** Tombol SOS raksasa di tengah beranda yang memerlukan sentuhan selama 3 detik penuh untuk mencegah ketidaksengajaan tertekan.
*   **Umpan Balik Visual & Animasi Denyut:** Setelah aktif, tombol akan memancarkan animasi pulsing denyut dinamis (scale 1.0x - 1.08x) dan teks status akan menyala merah berkedip.
*   **Notifikasi Broadcast Receiver:** Mengirimkan *system broadcast notification* lokal untuk memicu suara atau getaran tanda panggilan darurat terkirim.
*   **Deaktivasi Sekali Ketuk:** Panggilan darurat dapat dihentikan kapan saja dengan menekan tombol kembali sekali saja.

### 4. 📝 Lapor Kejadian (Formulir Pelaporan)
*   **Pelaporan Anonim Toggle:** Pengguna dapat memilih untuk melaporkan kejadian secara rahasia (anonim) demi keamanan privasi.
*   **Selector Kategori Kejadian Modern:** Menggunakan custom menu dropdown interaktif dengan animasi panah putar (berputar ke atas saat dibuka, kembali ke bawah saat ditutup/dipilih). Pilihan kategori mencakup:
    *   Pelecehan Seksual
    *   Kekerasan Fisik
    *   Kekerasan Verbal
    *   Stalking
    *   Cyber Bullying
    *   Diskriminasi, dsb.
*   **Kronologi & Upload Bukti:** Input text area untuk menceritakan kronologi lengkap kejadian secara detail dengan opsi menyertakan lampiran file pendukung.

### 5. 📊 Status Laporan (Monitoring CRUD)
*   **Tampilan Status Real-Time:** Menampilkan daftar laporan kejadian lengkap dengan detail ID unik (misalnya `#SR-2026-9137`), tanggal kejadian, dan kategori.
*   **Icon Status & Latar Belakang Custom:**
    *   **Terkirim:** Dilambangkan dengan icon surat (`mail.png`) berlatar biru.
    *   **Sedang Diproses:** Dilambangkan dengan icon jam pasir (`ic_proses.png`) berlatar kuning.
    *   **Selesai:** Dilambangkan dengan icon centang (`ic_selesai.png`) berlatar hijau.
*   **Progress Timeline Dinamis:**
    *   Laporan Baru/Terkirim: Hanya dot **Laporan Diterima** yang menyala hijau.
    *   Laporan Diproses: Dot **Laporan Diterima** dan **Investigasi Awal** menyala hijau.
    *   Laporan Selesai: Seluruh dot timeline menyala hijau lengkap dengan centang tanda proses berakhir.
*   **Operasi CRUD Penuh:** Memungkinkan pengguna mengedit deskripsi laporan yang dikirim atau menghapus laporan langsung dari daftar status.
*   **Non-Sticky Scrollable Footer:** Info akhir halaman *"Tidak ada laporan lainnya"* diletakkan di dalam `NestedScrollView` sehingga tidak menutupi daftar dan dapat digulir bebas dengan sisa margin bawah (`60dp`) yang rapi.

### 6. 📚 Edukasi Keamanan
*   **Layout Swapper Terintegrasi:** Menu 3-titik di bagian atas memungkinkan pengguna untuk mengubah mode tampilan konten edukasi keamanan antara:
    *   **List View:** Tampilan ringkas dengan cover image kecil di samping dan detail judul.
    *   **Grid View:** Tampilan dua kolom yang efisien.
    *   **Card View:** Tampilan kartu besar dengan cover image dominan.
*   **Asset Gambar Sesuai Tema:** Menggunakan aset cover ilustrasi bawaan (`img_stop_sa.webp`, `img_speak_out.webp`, dsb.) yang menggambarkan topik pencegahan dan penanganan kekerasan di kampus secara relevan.

### 7. ❓ FAQ (Frequently Asked Questions)
*   **Pencarian Filter Cepat:** Kolom pencarian dinamis di bagian atas untuk memfilter daftar pertanyaan secara langsung.
*   **Kartu Expand/Collapse:** Setiap pertanyaan dapat diklik untuk memperluas penjelasan jawaban secara interaktif.

---

## 🛠️ Stack Teknologi & Arsitektur
*   **Bahasa Pemrograman:** Kotlin 100% (Sesuai standard modern Android Development).
*   **Penyimpanan Data Lokal:** SQLite (`LaporanDbHelper.kt`) untuk operasi database relasional CRUD laporan kejadian.
*   **Komunikasi Antar Komponen:** Broadcast Receiver (`NotifikasiReceiver.kt`) untuk pemicu event sistem notifikasi darurat.
*   **Kompatibilitas SDK:** Min SDK 24, Target SDK 36 (Dukungan penuh Android 15/16).
