package com.techflow.materialcolor.helpers

import androidx.core.graphics.ColorUtils

/**
 * compare the given boolean value with user's check predicate
 *
 * @param this The boolean value to check predicate
 */
fun Int.isDark(): Boolean = ColorUtils.calculateLuminance(this) <= 0.5

/**
 * It compare boolean value with another boolean value
 *
 * @param value The secondary boolean value to check equality
 */
infix fun Boolean.eq(value: Boolean): Boolean = this == value