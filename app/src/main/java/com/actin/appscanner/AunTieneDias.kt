package com.actin.appscanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment

class AunTieneDias: AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Asistencia registrada, el usuario aun tiene dias")
            .setPositiveButton("Ok"){ dialogInterface : DialogInterface, i: Int ->

            }
        return builder.create()
    }
}