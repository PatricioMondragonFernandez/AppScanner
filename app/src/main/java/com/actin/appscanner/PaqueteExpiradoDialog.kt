package com.actin.appscanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class PaqueteExpiradoDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Paquete Expirado")
            .setMessage("El paquete del usuario esta expirado.")
            .setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->

            }
        return builder.create()
    }
}