package com.maikkkko1.android_base_arq.form_validator

import java.util.*

sealed class ValidatorRule {
    data class MustNotBeEmptyOrNull(val customErrorMessage: String? = null): ValidatorRule()
    data class MustHaveLetterAndNumbers(val customErrorMessage: String? = null): ValidatorRule()
    data class MustBeValidEmail(val customErrorMessage: String? = null): ValidatorRule()
    data class MustBeValidCreditCard(val customErrorMessage: String? = null): ValidatorRule()

    data class MustBeBetweenTwoIntegers(val firstValue: Int, val secondValue: Int): ValidatorRule()
    data class MustBeBetweenTwoDates(val firstDate: Date, val secondDate: Date): ValidatorRule()

    data class MustBeValidCondition(val condition: (() -> Boolean)): ValidatorRule()

    data class MustBeValidRegex(val regex: String): ValidatorRule()

    data class MustHaveMinimumLength(val size: Int): ValidatorRule()
    data class MustHaveMaximumLength(val size: Int): ValidatorRule()

    /** Canadian rules **/
    data class MustBeValidCanadianPostalCode(val customErrorMessage: String? = null): ValidatorRule()

    /** Brazilian rules **/
    data class MustBeValidBrazilianCPF(val customErrorMessage: String? = null): ValidatorRule()
}