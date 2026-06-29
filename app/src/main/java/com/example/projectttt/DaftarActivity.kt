package com.example.projectttt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DaftarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        val btnBack  = findViewById<ImageView>(R.id.btnBack)
        val btnDaftar= findViewById<Button>(R.id.btnDaftar)
        val tvKeLogin= findViewById<TextView>(R.id.tvKeLogin)

        btnBack.setOnClickListener  { finish() }
        tvKeLogin.setOnClickListener { finish() }

        val etNamaDaftar     = findViewById<android.widget.EditText>(R.id.etNamaDaftar)
        val etPasswordDaftar = findViewById<android.widget.EditText>(R.id.etPasswordDaftar)

        var isPasswordVisible = false
        etPasswordDaftar.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = etPasswordDaftar.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etPasswordDaftar.right - drawableEnd.bounds.width() - etPasswordDaftar.paddingRight)) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        etPasswordDaftar.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance()
                        etPasswordDaftar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
                    } else {
                        etPasswordDaftar.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
                        etPasswordDaftar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
                    }
                    etPasswordDaftar.setSelection(etPasswordDaftar.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        btnDaftar.setOnClickListener {
            val nama = etNamaDaftar.text.toString().trim()
            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama Lengkap harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan nama ke SharedPreferences
            val prefs = getSharedPreferences("AmanKampusPrefs", MODE_PRIVATE)
            prefs.edit().putString("USER_NAME", nama).apply()

            Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, BerandaActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }
    }
}
