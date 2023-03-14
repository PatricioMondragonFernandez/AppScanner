package com.actin.appscanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class pagarClaseDialog() : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("No hay paquete registrado")
            .setMessage("El usuario no tiene paquete registrado, debe paga su clase.")
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->

            }
        return builder.create()
    }
}