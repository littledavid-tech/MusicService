package top.littledavid.musicservice

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * Created by IT on 7/26/2018.
 */
val isDebug = true
val LOG_TAG = "LD-TAG"

fun String.logE() {
    if (isDebug)
        Log.e(LOG_TAG, this)
}

infix fun String.show(context: Context) = Toast.makeText(context, this, Toast.LENGTH_SHORT).show()