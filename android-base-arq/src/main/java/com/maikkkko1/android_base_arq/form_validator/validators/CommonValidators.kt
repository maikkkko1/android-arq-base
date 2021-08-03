package com.maikkkko1.android_base_arq.form_validator.validators

import android.text.TextUtils
import android.util.Patterns

fun isValidEmail(target: CharSequence?): Boolean = !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()

fun containsLetterAndNumbers(value: String?) = value?.matches("\\S*(\\S*([a-zA-Z]\\S*[0-9])|([0-9]\\S*[a-zA-Z]))\\S*".toRegex())
        ?: false

fun isValidCreditCard(cardNumber: String): Boolean {
    var s1 = 0
    var s2 = 0
    val reverse = StringBuffer(cardNumber).reverse().toString()

    for (i in reverse.indices) {
        val digit = Character.digit(reverse[i], 10)
        when {
            i % 2 == 0 -> s1 += digit
            else -> {
                s2 += 2 * digit
                when {
                    digit >= 5 -> s2 -= 9
                }
            }
        }
    }

    return (s1 + s2) % 10 == 0
}
