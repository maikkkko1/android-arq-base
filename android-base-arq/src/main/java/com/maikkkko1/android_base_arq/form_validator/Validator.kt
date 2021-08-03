package com.maikkkko1.android_base_arq.form_validator

import com.maikkkko1.android_base_arq.form_validator.entity.RuleValidationResult
import com.maikkkko1.android_base_arq.form_validator.validators.*
import java.util.*

class Validator constructor(private val value: Any?) {
    fun isValidBasedOnRules(rulesToValidate: List<ValidatorRule>): RuleValidationResult {
        for (rule in rulesToValidate) {
            when (rule) {
                is ValidatorRule.MustHaveLetterAndNumbers -> {
                    if (!containsLetterAndNumbers(value?.toString())) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must contain letters and numbers!")
                    }
                }

                is ValidatorRule.MustHaveMinimumLength -> {
                    if (value?.toString()?.length ?: 0 < rule.size) {
                        return RuleValidationResult(isValid = false, message = "Size must be at least ${rule.size}!")
                    }
                }

                is ValidatorRule.MustHaveMaximumLength -> {
                    if (value?.toString()?.length ?: 0 > rule.size) {
                        return RuleValidationResult(isValid = false, message = "Size must be a maximum of ${rule.size}!")
                    }
                }

                is ValidatorRule.MustNotBeEmptyOrNull -> {
                    if (value?.toString().isNullOrEmpty()) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must not be empty or null!")
                    }
                }

                is ValidatorRule.MustBeValidEmail -> {
                    if (!isValidEmail(value?.toString())) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must be a valid email!")
                    }
                }

                is ValidatorRule.MustBeValidRegex -> {
                    if (!value.toString().matches(rule.regex.toRegex())) {
                        return returnMustBeAValidValue()
                    }
                }

                is ValidatorRule.MustBeValidCondition -> {
                    if (!rule.condition.invoke()) {
                        return returnMustBeAValidValue()
                    }
                }

                is ValidatorRule.MustBeBetweenTwoIntegers -> {
                    val valueToNumber = value as? Int ?: 0

                    if (valueToNumber < rule.firstValue && valueToNumber > rule.secondValue) {
                        return RuleValidationResult(isValid = false, message = "Must be a value between ${rule.firstValue} and ${rule.secondValue}!")
                    }
                }

                is ValidatorRule.MustBeBetweenTwoDates -> {
                    val valueToDate = value as? Date


                   if (valueToDate != null) {
                       if (!valueToDate.before(rule.firstDate) || !valueToDate.after(rule.secondDate)) {
                           return returnMustBeAValidValue()
                       }
                   } else {
                       return returnMustBeAValidValue()
                   }
                }

                is ValidatorRule.MustBeValidCreditCard -> {
                    if (!isValidCreditCard(value?.toString() ?: "")) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must be a valid credit card!")
                    }
                }

                is ValidatorRule.MustBeValidCanadianPostalCode -> {
                    if (!isValidCanadianPostalCode(value?.toString() ?: "")) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must be a valid canadian postal code!")
                    }
                }

                is ValidatorRule.MustBeValidBrazilianCPF -> {
                    if (!isValidCPF(value?.toString() ?: "")) {
                        return RuleValidationResult(isValid = false, message = rule.customErrorMessage ?: "Must be a valid brazilian CPF!")
                    }
                }

            }
        }

        return RuleValidationResult(isValid = true, message = null)
    }

    private fun returnMustBeAValidValue() = RuleValidationResult(isValid = false, message = "Must be a valid value!")
}