package io.github.bikkysamuel.animetv.utils

import android.util.Log
import java.util.Locale

class MyLogger {
    companion object {
        fun <T: Any> d(classToTag: T, msg: String?) {
            val tag = classToTag::class.java.simpleName
            Log.d(tag, msg!!)
        }

        fun <T: Any> d(classToTag: T, msg: String?, vararg args: Any?) {
            d(classToTag, String.format(Locale.US, msg!!, *args))
        }
    }
}