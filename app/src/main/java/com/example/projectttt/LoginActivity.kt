package com.example.projectttt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin      = findViewById<Button>(R.id.btnLogin)
        val tvKeDaftar    = findViewById<TextView>(R.id.tvKeDaftar)
        val tvLupaPassword= findViewById<TextView>(R.id.tvLupaPassword)

        val etEmail       = findViewById<android.widget.EditText>(R.id.etEmail)
        val etPassword    = findViewById<android.widget.EditText>(R.id.etPassword)

        var isPasswordVisible = false
        etPassword.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableEnd = etPassword.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etPassword.right - drawableEnd.bounds.width() - etPassword.paddingRight)) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        etPassword.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
                    } else {
                        etPassword.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
                    }
                    etPassword.setSelection(etPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                // If login email is provided, let's extract a friendly username from it just in case
                val nameFromEmail = email.substringBefore("@").replace(".", " ").capitalize()
                val prefs = getSharedPreferences("AmanKampusPrefs", MODE_PRIVATE)
                if (prefs.getString("USER_NAME", "").isNullOrEmpty()) {
                    prefs.edit().putString("USER_NAME", nameFromEmail).apply()
                }
            }
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }

        tvKeDaftar.setOnClickListener {
            startActivity(Intent(this, DaftarActivity::class.java))
        }

        tvLupaPassword.setOnClickListener {
            Toast.makeText(this, "Fitur Lupa Password segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }
}
