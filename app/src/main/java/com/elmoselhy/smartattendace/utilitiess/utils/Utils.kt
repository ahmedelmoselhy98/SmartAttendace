package com.elmoselhy.smartattendace.utilitiess.utils

import android.os.Handler
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun formatDate(date: Date): String {
            var dateStr = ""
            try {
                val formatter: DateFormat = SimpleDateFormat(
                    "dd_MM_yyyy", Locale.ENGLISH
                ) //If you need time just put specific format for time like 'HH:mm:ss'
                dateStr = formatter.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return dateStr
        }

        fun formatStringDate(dateString: String) {
            var dateStr = ""
            if (dateString != null) {
                val dateFormat: DateFormat = SimpleDateFormat("dd_MM_dd")
                var date: Date? =
                    null //You will get date object relative to server/client timezone wherever it is parsed
                try {
                    //  Sat 10 November 2018
                    date = dateFormat.parse(dateString)
                    val formatter: DateFormat = SimpleDateFormat(
                        "EEE d MMM yyyy-HH:mm",
                    ) //If you need time just put specific format for time like 'HH:mm:ss'
                    dateStr = formatter.format(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
        }

        fun executeDelay(milliseconds: Long, onExecute: () -> Unit) {
            Handler().postDelayed({
                onExecute()
            }, milliseconds)
        }
    }
}
