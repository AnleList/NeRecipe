package ru.netology.nerecipe.data

import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Recipe(
    val id: Long,
    val author: String,
    val recipeName: String,
    val recipeCategory: RecipeCategories,
    val ingredients: String,
    val stages: String,
    val draftTextContent: String?,
    val videoContent: String?,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Int = 0,
    var sharedByMe: Boolean = false,
    var shared: Int = 0,
) : Parcelable
