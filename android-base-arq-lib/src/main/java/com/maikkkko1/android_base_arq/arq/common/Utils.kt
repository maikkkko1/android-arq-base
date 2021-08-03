package com.maikkkko1.android_base_arq.arq.common

import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun isValidPostalCode(code: String): Boolean {
            return code.matches("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$".toRegex())
        }

        fun formatMonetary(value: Number): String {
            return NumberFormat.getCurrencyInstance(Locale("en", "CA")).format(value)
        }
    }
}
