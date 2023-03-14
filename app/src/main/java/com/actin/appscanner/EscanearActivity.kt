package com.actin.appscanner

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.security.AccessController.getContext

class EscanearActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var btnRegresar: Button
    private lateinit var scannerView: CodeScannerView
    private lateinit var cambiarCamara: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escanear)

        btnRegresar = findViewById(R.id.btnRegresar)
        scannerView = findViewById(R.id.scannerView)
        cambiarCamara = findViewById(R.id.cambiarCamara)
        var contador = 0

        codeScanner = CodeScanner(this,scannerView)


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 123)
        }else{
            startScanning()
        }
        btnRegresar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java ))
        }
        cambiarCamara.setOnClickListener{
            if (contador % 2 == 0){
                codeScanner.camera = CodeScanner.CAMERA_BACK
                contador += 1
            }else{
                codeScanner.camera = CodeScanner.CAMERA_FRONT
                contador += 1
            }
        }
    }


    private fun startScanning() {
        val scannerView: CodeScannerView = findViewById(R.id.scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            CoroutineScope(Dispatchers.IO).launch {
                val url = URL("https://actinseguro.com/booking/abkcom012.aspx?${it.text}")
                val conn =url.openConnection()

                val datos = StringBuffer()

                BufferedReader(InputStreamReader(conn.getInputStream())).use { inp ->
                    var line: String?
                    while (inp.readLine().also { line = it } != null) {
                        println(line)
                        datos.append(line)
                    }
                }
                val json = datos.toString()
                println(json)
                val objeto = JSONObject(json)
                val array = objeto.getJSONArray("ASISTENCIA")
                val indice0 = array.getJSONObject(0)
                val msg = indice0.getString("MSG")
                val delim = " "
                val arregloMsg = msg.split(delim)
                println(arregloMsg[0])
                println(arregloMsg[1])

                if (msg == "ASISTENCIA REGISTRADA"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                        val dialog = asistenciaRegistradaDialog()
                        dialog.show(supportFragmentManager, "Asistencia Registrada.")
                    }
                }else if(msg == "ASISTENCIA REGISTRADA El usuario no cuenta con paquete asignado"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                        val dialog = pagarClaseDialog()
                        dialog.show(supportFragmentManager, "El usuario debe pagar su clase.")
                    }

                }else if(msg == "No se Encontro registro de reserva (La asistencia solo se puede registras 15 minutos anes de iniciar la clase o 10 minutos despues de haber iniciado)"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = noSeEncontroAsistenciaDialog()
                        dialog.show(supportFragmentManager, "No se encontro asistencia, La asistencia solo puede registrarse 15 minutos antes de iniciar la clase o 10 minutos despues de haber iniciado..")
                    }
                }else if(msg == "ASISTENCIA REGISTRADA El ultimo paquete registrado ha expirado"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = PaqueteExpiradoDialog()
                        dialog.show(supportFragmentManager, "Paquete Expirado.")
                    }
                }else if(msg == "ASISTENCIA REGISTRADA Aun tiene dias") {
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = AunTieneDias()
                        dialog.show(supportFragmentManager, "Aun tiene dias.")
                    }
                } else if(msg == "ASISTENCIA REGISTRADA El usuario ya ha usado las clases de su paquete"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = YaHaUsadoLasClases()
                        dialog.show(supportFragmentManager, "Ya ha usado sus clases.")
                    }

                }else if(msg == "ASISTENCIA REGISTRADA El tipo de salon de la clase no concuerda con el salon del paquete"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = SalonNoConcuerda()
                        dialog.show(supportFragmentManager, "Salón no concuerda.")
                    }

                }else if(msg == "ASISTENCIA REGISTRADA Paquete ilimitado o de autopago"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = PaqueteIlimitado()
                        dialog.show(supportFragmentManager, "Paquete ilimitado.")
                    }
                }else if(arregloMsg[0] == "ASISTENCIA" && arregloMsg[1] == "REGISTRADA"){
                    println(json)
                    runOnUiThread {
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
                        val dialog = asistenciaRegistradaDialog()
                        dialog.show(supportFragmentManager, "Asistencia Registrada.")
                    }
                }else{
                    runOnUiThread {
                        println(json)
                        Toast.makeText(baseContext, "Scan result: ${it.text}", Toast.LENGTH_LONG)
                            .show()
                        val dialog = noSeEncontroAsistenciaDialog()
                        dialog.show(supportFragmentManager, "No se encontro asistencia.")
                    }
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera permission granted ", Toast.LENGTH_SHORT).show()
                startScanning()
            }else{
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized){
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized){
            codeScanner?.releaseResources()
        }
        super.onPause()
    }
}