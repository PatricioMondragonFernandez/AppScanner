package com.actin.appscanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class SalonNoConcuerda: AppCompatDialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Asistencia registrada, el tipo de salón de la clase no concuerda con el salón del paquete.")
            .setPositiveButton("Ok"){ dialogInterface : DialogInterface, i: Int ->

            }
        return builder.create()
    }
}