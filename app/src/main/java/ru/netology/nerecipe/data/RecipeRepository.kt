package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData

interface RecipeRepository {

    val data: LiveData<List<Recipe>>
    var filter: String?


    fun getAll(filter: RecipeFilter
//        filter: String?, categories: List<RecipeCategories>
    ): LiveData<List<Recipe>>
    fun likeById(recipeId: Long)
    fun shareBiId(recipeId: Long)
    fun removeById(recipeId: Long)
    fun save(recipe: Recipe)
    fun changeFilter(filter: String): LiveData<List<Recipe>>
    fun moveRecipeToPosition(recipe: Recipe, destinationPosition: Long)
    fun countOfRecipes(): Long
}