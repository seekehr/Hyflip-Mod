package com.hyflip.mod.utils

import java.text.DecimalFormat

object ItemUtils {
    fun convertItemIdToSearchableName(itemId: String): String {
        return itemId.split("_").first().lowercase()
    }
    fun formatPrice(value: Double): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(value)
    }

    fun formatInts(value: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(value)
    }
}