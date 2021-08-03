package com.maikkkko1.android_base_arq.form_validator.validators

fun isValidCanadianPostalCode(code: String): Boolean = code.matches("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$".toRegex())