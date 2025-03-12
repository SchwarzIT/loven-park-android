package com.schwarzit.lovenpark.mappin.domain

enum class PinCategory {
    BENCH,
    BICYCLE,
    TRASH,
    OTHER
}

fun String.getPinCategoryEnum() = if (PinCategory.values().any { it.name == this }) {
    PinCategory.valueOf(this)
} else {
    PinCategory.OTHER
}