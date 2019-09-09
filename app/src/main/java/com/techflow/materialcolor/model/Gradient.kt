package com.techflow.materialcolor.model

data class Gradient(var type: Int, var primaryColor: String, var secondaryColor: String) {
    companion object {
        const val TYPE_GRADIENT = 0
        const val TYPE_AD = 1
        const val TYPE_SECTION = 2
    }
}