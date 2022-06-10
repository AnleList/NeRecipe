package ru.netology.nerecipe.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import ru.netology.nerecipe.adapters.RecipeInteractionListener
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.util.SingleLiveEvent

class RecipeViewModel(
    application: Application
): AndroidViewModel(application), RecipeInteractionListener {

    private val changeFilter = MutableLiveData<String?>(null)
    private val filterByCategory = MutableLiveData(
        listOf(
            RecipeCategories.Other,
            RecipeCategories.Russian,
            RecipeCategories.Eastern,
            RecipeCategories.European,
            RecipeCategories.Mediterranean,
            RecipeCategories.Panasian,
            RecipeCategories.American,
            RecipeCategories.Asian,
        )
    )

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).recipeDao,
        filter = null
        )

    val data = changeFilter.switchMap{filter ->
        repository.getAll(filter, filterByCategory.value!!)

}

//    val sharePostContent = SingleLiveEvent<String>()
    val navToRecipeViewing = MutableLiveData<Recipe>()
    val navToRecipeEdit = MutableLiveData<Recipe>()
    private val navToFeedFragment = SingleLiveEvent<Unit>()
    val currentRecipe = MutableLiveData<Recipe?>(null)
//    val sharePostVideo = SingleLiveEvent<String?>()


//    fun onEditBackPressed(draft: String){
//        if (currentRecipe.value != null) {
//            currentRecipe.value?.copy(
//                draftTextContent = draft
//            )?.let {
//                repository.save(it)
//            }
//            currentRecipe.value = null
//        }
//    }

    override fun saveRecipe(recipeToSave: Recipe) {
        repository.save(recipeToSave)
    }

    override fun onHeartClicked(recipe: Recipe) =
        repository.likeById(recipe.id)

    override fun recipeUp(recipe: Recipe) {
        if (recipe.id == 1L) return else
        repository.moveRecipeToPosition(recipe, recipe.id - 1L)
    }

    override fun recipeDown(recipe: Recipe) {
        if (recipe.id == repository.countOfRecipes()) return else
        repository.moveRecipeToPosition(recipe, recipe.id + 1L)
    }

    override fun inFilterChange(filter: String) {
        changeFilter.value = filter
    }

//    override fun onShareClicked(recipe: Recipe) {
//        sharePostContent.value = recipe.ingredients
//        repository.shareBiId(recipe.id)
//    }

    override fun onRemoveClicked(recipe: Recipe) {
        repository.removeById(recipe.id)
        navToFeedFragment.call()
    }

    override fun stageUp(stage: Stage) {
        val stagesToSave: MutableList<Stage> =
            currentRecipe.value?.stages?.toMutableList() ?: return
        val destinationStage: Stage
        if (stage.id == 1) return else {
            destinationStage = stagesToSave[stage.id-2]
            stagesToSave[stage.id-2] = stage.copy(id = destinationStage.id)
            stagesToSave[stage.id-1] = destinationStage.copy(id = stage.id)
        }
        repository.save(currentRecipe.value!!.copy(stages = stagesToSave))
        currentRecipe.value = currentRecipe.value!!.copy(stages = stagesToSave)
    }

    override fun stageDown(stage: Stage) {
        val stagesToSave: MutableList<Stage> =
            currentRecipe.value?.stages?.toMutableList() ?: return
        val destinationStage: Stage
        if (stage.id >= stagesToSave.size) return else {
            destinationStage = stagesToSave[stage.id]
            stagesToSave[stage.id] = stage.copy(id = destinationStage.id)
            stagesToSave[stage.id-1] = destinationStage.copy(id = stage.id)
        }
        repository.save(currentRecipe.value!!.copy(stages = stagesToSave))
        currentRecipe.value = currentRecipe.value!!.copy(stages = stagesToSave)
    }

    override fun deleteStage(stage: Stage) {
        val stagesToSave: MutableList<Stage> =
            currentRecipe.value?.stages?.filter {it.id != stage.id} as MutableList<Stage>
        for ((index, eachStage) in stagesToSave.withIndex()) {
            eachStage.id = index + 1
        }
        repository.save(currentRecipe.value!!.copy(stages = stagesToSave))
        currentRecipe.value = currentRecipe.value!!.copy(stages = stagesToSave)
    }

    override fun editRecipe(recipe: Recipe) {
        currentRecipe.value = recipe
        navToRecipeEdit.value = recipe
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