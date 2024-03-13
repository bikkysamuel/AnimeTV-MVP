package io.github.bikkysamuel.animetv.utils

import android.widget.Toast
import io.github.bikkysamuel.animetv.app.MyApp

class ShowToast {

    companion object {
        fun showShortToastMsg(msg: String) {
            Toast.makeText(
                MyApp.instance.applicationContext,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}