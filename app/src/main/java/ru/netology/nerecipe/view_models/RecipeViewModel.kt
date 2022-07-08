package ru.netology.nerecipe.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import ru.netology.nerecipe.adapters.RecipeInteractionListener
import ru.netology.nerecipe.data.*
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
): AndroidViewModel(application), RecipeInteractionListener {

    private val filterByCategory = MutableLiveData<List<RecipeCategories>>(emptyList())
    private val filterByName = MutableLiveData<String>(null)
    private val filterByLikedByMe = MutableLiveData<Boolean>(false)
    private val recipeFilter = MutableLiveData(
        filterByCategory.value?.let {
            filterByLikedByMe.value?.let { it1 -> RecipeFilter(filterByName.value, it, it1) }
        }
    )
    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).recipeDao,
        )
    val data = recipeFilter.switchMap{ recipeFilter ->
        repository.getAll(recipeFilter)
    }

    val navToRecipeViewing = SingleLiveEvent<Recipe>()
    val navToRecipeEdit = SingleLiveEvent<Recipe>()
    private val navToFeedFragment = SingleLiveEvent<Unit>()
    val currentRecipe = MutableLiveData<Recipe>()

    override fun saveRecipe(recipeToSave: Recipe) {
        repository.save(recipeToSave)
    }

    override fun onHeartClicked(recipe: Recipe) =
        repository.likeById(recipe.id)

    override fun recipeUp(recipeID: Long) {
        if (recipeID == 1L) return else
        repository.moveRecipeToPosition(recipeID, recipeID - 1L)
    }

    override fun moveRecipe(from: Long, to: Long) {
        repository.moveRecipeToPosition(from, to)
    }

    override fun recipeDown(recipeID: Long) {
        if (recipeID == repository.countOfRecipes()) return else
        repository.moveRecipeToPosition(recipeID, recipeID + 1L)
    }

    override fun inFilterByNameChange(filter: String) {
        filterByName.value = filter
        recipeFilter.value = filterByCategory.value?.let {
            filterByLikedByMe.value?.let { it1 -> RecipeFilter(filterByName.value, it, it1) }
        }
    }

    override fun inFilterByCategoryChange(receivedCategory: RecipeCategories) {
        var listCategory: List<RecipeCategories>? = filterByCategory.value
        var isCategoryInList = false
        listCategory?.map { category -> if (category == receivedCategory) isCategoryInList = true }
        if (listCategory != null) {
            listCategory = if (isCategoryInList) listCategory.filter { it != receivedCategory }
            else listCategory + receivedCategory
        }
        filterByCategory.value = listCategory
        recipeFilter.value = filterByCategory.value?.let {
            filterByLikedByMe.value?.let { it1 -> RecipeFilter(filterByName.value, it, it1) }
        }
    }

    override fun inFilterByLikedByMeChange(isNeedOnlyLikedByMe: Boolean) {
        filterByLikedByMe.value = isNeedOnlyLikedByMe
        recipeFilter.value = filterByCategory.value?.let {
                filterByLikedByMe.value?.let { it1 -> RecipeFilter(filterByName.value, it, it1) }
            }
    }


    override fun removeRecipeById(recipeID: Long) {
        repository.removeById(recipeID)
        navToFeedFragment.call()
    }

    override fun moveStage(from: Int, to: Int) {
        val stagesToSave: MutableList<Stage> =
            currentRecipe.value?.stages?.toMutableList() ?: return
        val destinationStage: Stage
        val movableStage: Stage
        if (from == 1 || to >= stagesToSave.size) return else {
            destinationStage = stagesToSave[to]
            movableStage = stagesToSave[from]
            stagesToSave[to] = movableStage.copy(id = destinationStage.id)
            stagesToSave[from] = destinationStage.copy(id = movableStage.id)
        }
        val recipeToSave: Recipe? = currentRecipe.value
        if (recipeToSave != null) {
            repository.save(recipeToSave.copy(stages = stagesToSave))
            currentRecipe.value = recipeToSave.copy(stages = stagesToSave)
        }
    }

    override fun deleteStage(position: Int) {
        val stagesToSave: MutableList<Stage> =
            currentRecipe.value?.stages?.filter {it.id != position+1} as MutableList<Stage>
        for ((index, eachStage) in stagesToSave.withIndex()) {
            eachStage.id = index + 1
        }
        val recipeToSave: Recipe? = currentRecipe.value
        if (recipeToSave != null) {
            repository.save(recipeToSave.copy(stages = stagesToSave))
            currentRecipe.value = recipeToSave.copy(stages = stagesToSave)
        }
    }

    override fun editRecipe(recipe: Recipe) {
        currentRecipe.value = recipe
    }

    override fun onUnDoClicked() {
        currentRecipe.value = null
    }

    override fun onAddClicked() {
        navToRecipeEdit.value =
            Recipe(0,"","",RecipeCategories.Other,"", emptyList(),
                null, "", false, 0 ,false, 0)
    }

    override fun navToRecipeViewFun(recipe: Recipe) {
        currentRecipe.value = recipe
        navToRecipeViewing.value = recipe
    }
}