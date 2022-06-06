package ru.netology.nerecipe.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import ru.netology.nerecipe.adapters.RecipeInteractionListener
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeRepository
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.impl.RecipeRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class RecipeViewModel(
    application: Application
): AndroidViewModel(application), RecipeInteractionListener {

    private val changeFilter = MutableLiveData<String?>(null)

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(context = application).postDao,
        filter = null
        )

//    val data by repository::data
    val data = changeFilter.switchMap{filter ->
        repository.getAll(filter)

}

//    val sharePostContent = SingleLiveEvent<String>()
    val navToRecipeViewing = SingleLiveEvent<Recipe>()
    val navToPostEditContentEvent = SingleLiveEvent<String>()
    private val navToFeedFragment = SingleLiveEvent<Unit>()
    private val currentRecipe = MutableLiveData<Recipe?>(null)
//    val sharePostVideo = SingleLiveEvent<String?>()


    fun onEditBackPressed(draft: String){
        if (currentRecipe.value != null) {
            currentRecipe.value?.copy(
                draftTextContent = draft
            )?.let {
                repository.save(it)
            }
            currentRecipe.value = null
        }
    }

    fun onSaveClicked(content: String) {
        if (content.isBlank()) return
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("LOCALIZE"))
        val recipeToAdd = currentRecipe.value?.copy(
            ingredients = content,
            draftTextContent = null
            ) ?: Recipe(
            id = RecipeRepository.NEW_POST_ID,
            author = "New author",
            ingredients = content,
            draftTextContent = null,
            videoContent = null,
            published = (sdf.format(Date())).toString(),
            category = RecipeCategories.Russian,
            name = "",
            stages = emptyList()
        )
        repository.save(recipeToAdd)
        currentRecipe.value = null
    }

//    override fun onShareVideoClicked(recipe: Recipe) {
//        sharePostVideo.value = recipe.videoContent
//    }

    override fun onHeartClicked(recipe: Recipe) =
        repository.likeById(recipe.id)

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

    override fun onEditClicked(recipe: Recipe) {
        navToPostEditContentEvent.value =
            recipe.draftTextContent ?: recipe.ingredients
        currentRecipe.value = recipe
    }

    override fun onUnDoClicked() {
        currentRecipe.value = null
    }

    override fun onAddClicked() {
      navToPostEditContentEvent.call()
    }

    override fun navToRecipeViewFun(recipe: Recipe) {
        navToRecipeViewing.value = recipe
    }
}