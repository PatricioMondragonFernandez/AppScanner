package com.actin.appscanner

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.actin.appscanner.databinding.ActivityMainBinding
import com.budiyev.android.codescanner.*
import com.budiyev.android.codescanner.R
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var btnRegresar: Button
    private lateinit var btnEscanear: Button
    private lateinit var scannerView: CodeScannerView

    private lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEscanear.setOnClickListener {
            startActivity(Intent(this, EscanearActivity::class.java))
        }
    }
}