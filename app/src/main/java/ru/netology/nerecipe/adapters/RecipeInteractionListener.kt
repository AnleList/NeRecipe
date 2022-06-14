package ru.netology.nerecipe.adapters

import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage

interface RecipeInteractionListener {
    fun onHeartClicked(recipe: Recipe)
    fun inFilterByNameChange(filter: String)
    fun inFilterByCategoryChange(receivedCategory: RecipeCategories)
    fun editRecipe(recipe: Recipe)
    fun saveRecipe(recipeToSave: Recipe)
    fun onUnDoClicked()
    fun onAddClicked()
    //    fun onShareVideoClicked(recipe: Recipe)
    fun navToRecipeViewFun(recipe: Recipe)
    fun recipeDown(recipeID: Long)
    fun recipeUp(recipeID: Long)
    fun moveRecipe(from: Long, to: Long)
    fun removeRecipeById(recipeID: Long)
    fun stageUp(stage: Stage)
    fun stageDown(stage: Stage)
    fun deleteStage(stage: Stage)
//    fun onShareClicked(recipe: Recipe)
}

