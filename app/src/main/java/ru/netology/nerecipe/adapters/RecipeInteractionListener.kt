package ru.netology.nerecipe.adapters

import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.Stage

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
    fun stageUp(stage: Stage)
    fun stageDown(stage: Stage)
    fun deleteStage(stage: Stage)
//    fun onShareClicked(recipe: Recipe)
}

