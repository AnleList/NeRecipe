package ru.netology.nerecipe.adapters

import ru.netology.nerecipe.data.Recipe

interface RecipeInteractionListener {
    fun onHeartClicked(recipe: Recipe)
//    fun onShareClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
    fun onUnDoClicked()
    fun onAddClicked()
//    fun onShareVideoClicked(recipe: Recipe)
    fun onPostContentClicked(recipe: Recipe)
}