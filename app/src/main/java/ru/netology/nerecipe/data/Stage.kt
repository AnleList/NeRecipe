package ru.netology.nerecipe.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Stage(
    val id: Int,
    val text: String,
    val imageURL: String,
) : Parcelable
