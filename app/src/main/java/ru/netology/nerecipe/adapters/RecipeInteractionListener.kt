package ru.netology.nerecipe.adapters

import ru.netology.nerecipe.data.Recipe

interface RecipeInteractionListener {
    fun onHeartClicked(recipe: Recipe)
    fun inFilterChange(filter: String)
    fun editRecipe(recipe: Recipe)
    fun saveRecipe(recipeToSave: Recipe)
    fun onUnDoClicked()
    fun onAddClicked()
    //    fun onShareVideoClicked(recipe: Recipe)
    fun navToRecipeViewFun(recipe: Recipe)
    fun recipeDown(recipe: Recipe)
    fun recipeUp(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
//    fun onShareClicked(recipe: Recipe)
}

