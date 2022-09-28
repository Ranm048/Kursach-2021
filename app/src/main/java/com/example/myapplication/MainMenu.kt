package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    private lateinit var btnChats: Button
    private lateinit var btnUsecrypto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        btnChats=findViewById(R.id.btnChats)
        btnUsecrypto=findViewById(R.id.btnUsecrypto)
        btnChats.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
        }
        btnUsecrypto.setOnClickListener{
            val intent = Intent(this, CryptoActivity::class.java)
            startActivity(intent)
        }
    }
}