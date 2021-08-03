package com.maikkkko1.android_base_arq.form_validator

import android.graphics.drawable.Drawable
import com.maikkkko1.android_base_arq.form_validator.entity.FormValidatorDefaultInput
import com.maikkkko1.android_base_arq.form_validator.entity.FormValidatorMaterialInput

object FormValidator {
    fun validate(inputs: List<FormValidatorMaterialInput>, showInputError: Boolean = false, isBorderlessInput: Boolean = false, errorIcon: Drawable? = null): Boolean =
        validateMaterialInput(
            inputs = inputs,
            showInputError = showInputError,
            errorIcon = errorIcon,
            isBorderlessInput = isBorderlessInput
        )

    fun validate(inputs: List<FormValidatorDefaultInput>, showInputError: Boolean = false): Boolean =
        validateDefaultInput(
            inputs = inputs,
            showInputError = showInputError
        )

    private fun validateMaterialInput(inputs: List<FormValidatorMaterialInput>, showInputError: Boolean, isBorderlessInput: Boolean, errorIcon: Drawable? = null): Boolean {
        var isAtLeastOneInputWithError = false

        for (item in inputs) {
            val value = item.input.text.toString()

            val validateForm = Validator(
                value
            ).isValidBasedOnRules((item.rules))

            if (!validateForm.isValid) {
                if (!isAtLeastOneInputWithError) {
                    isAtLeastOneInputWithError = true
                }

                if (showInputError) {
                    item.container.apply {
                        errorIconDrawable = errorIcon

                        error = validateForm.message
                        if (isBorderlessInput) boxStrokeWidth = 6
                    }

                    item.container.error = validateForm.message
                } else item.container.error = null
            } else {
                item.container.error = null
                if (isBorderlessInput) item.container.boxStrokeWidth = 0
            }
        }

        return !isAtLeastOneInputWithError
    }

    private fun validateDefaultInput(inputs: List<FormValidatorDefaultInput>, showInputError: Boolean): Boolean {
        var isAtLeastOneInputWithError = false

        for (item in inputs) {
            val value = item.input.text.toString()

            val validateForm = Validator(
                value
            ).isValidBasedOnRules((item.rules))

            if (!validateForm.isValid) {
                if (!isAtLeastOneInputWithError) {
                    isAtLeastOneInputWithError = true
                }

                if (showInputError) item.input.error = validateForm.message else item.input.error = null
            } else {
                item.input.error = null
            }
        }

        return !isAtLeastOneInputWithError
    }
}