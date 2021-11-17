package net.dixq.overlaypainter

import android.util.Log
import java.util.*

class Lg {

    companion object {

        private const val TAG = "OverlayPainter"

        fun e(msg: String) {
            Log.e(TAG, getMsg(msg))
        }

        fun w(msg: String) {
            Log.w(TAG, getMsg(msg))
        }

        fun i(msg: String) {
            Log.i(TAG, getMsg(msg))
        }

        fun d(msg: String) {
            Log.d(TAG, getMsg(msg))
        }

        private fun getMsg(msg: String): String {
            val className = Thread.currentThread().stackTrace[4].className
            return String.format(
                "%s | %s#%s(%d) %s",
                msg,
                className.substring(className.lastIndexOf('.') + 1),
                Thread.currentThread().stackTrace[4].methodName,
                Thread.currentThread().stackTrace[4].lineNumber,
                getTime()
            )
        }

        private fun getTime(): String? {
            val c = Calendar.getInstance()
            val tzn2 = TimeZone.getTimeZone("Asia/Tokyo")
            c.timeZone = tzn2
            c.time = Date(System.currentTimeMillis())
            return java.lang.String.format(
                " | %02d/%02d %02d:%02d:%02d",
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND)
            )
        }
    }
}