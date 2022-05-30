package ru.netology.nerecipe.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nerecipe.data.RecipeCategories


@Entity(tableName = "posts")
class PostEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "recipeName")
    val recipeName: String,
    @ColumnInfo(name = "recipeCategory")
    val recipeCategory: RecipeCategories,
    @ColumnInfo(name = "ingredients")
    val ingredients: String,
    @ColumnInfo(name = "stages")
    val stages: String,
    @ColumnInfo(name = "draftTextContent")
    val draftTextContent: String?,
    @ColumnInfo(name = "videoContent")
    val videoContent: String?,
    @ColumnInfo(name = "published")
    val published: String,
    @ColumnInfo(name = "likedByMe")
    var likedByMe: Boolean = false,
    @ColumnInfo(name = "likes")
    var likes: Int = 0,
    @ColumnInfo(name = "sharedByMe")
    var sharedByMe: Boolean = false,
    @ColumnInfo(name = "shared")
    var shared: Int = 0,
)