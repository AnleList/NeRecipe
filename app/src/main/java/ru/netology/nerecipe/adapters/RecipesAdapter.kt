package ru.netology.nerecipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.databinding.RecipeCardLayoutBinding
import ru.netology.nerecipe.valueToStringForShowing

internal class RecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeCardLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder (
        private val binding: RecipeCardLayoutBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.recipeCardMenuButton).apply {
                inflate(R.menu.options_recipe)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.removeItem -> {
                            listener.onRemoveClicked(recipe)
                            true
                        }
                        R.id.editItem -> {
                            listener.editRecipe(recipe)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.recipeCardHeart.setOnClickListener {
                listener.onHeartClicked(recipe)
            }
            binding.recipeName.setOnClickListener {
                listener.navToRecipeViewFun(recipe)
            }
            binding.recipeAuthor.setOnClickListener {
                listener.navToRecipeViewFun(recipe)
            }
            binding.recipeCategory.setOnClickListener {
                listener.navToRecipeViewFun(recipe)
            }
            binding.recipeName.setOnLongClickListener {
                binding.fabUp.visibility = View.VISIBLE
                binding.fabDown.visibility = View.VISIBLE
                binding.fabSetInvisible.visibility = View.VISIBLE
                return@setOnLongClickListener true
            }
            binding.recipeAuthor.setOnLongClickListener {
                binding.fabUp.visibility = View.VISIBLE
                binding.fabDown.visibility = View.VISIBLE
                binding.fabSetInvisible.visibility = View.VISIBLE
                return@setOnLongClickListener true
            }
            binding.recipeCategory.setOnLongClickListener {
                binding.fabUp.visibility = View.VISIBLE
                binding.fabDown.visibility = View.VISIBLE
                binding.fabSetInvisible.visibility = View.VISIBLE
                return@setOnLongClickListener true
            }
            binding.fabUp.setOnClickListener {
                listener.recipeUp(recipe)
            }
            binding.fabDown.setOnClickListener {
                listener.recipeDown(recipe)
            }
            binding.fabSetInvisible.setOnClickListener {
                binding.fabUp.visibility = View.INVISIBLE
                binding.fabDown.visibility = View.INVISIBLE
                binding.fabSetInvisible.visibility = View.INVISIBLE
            }
            binding.recipeCardMenuButton.setOnClickListener {
                popupMenu.show()
            }
//            binding.share.setOnClickListener {
//                listener.onShareClicked(recipe)
//            }
//            binding.fabVideo.setOnClickListener {
//                listener.onShareVideoClicked(recipe)
//            }
//            binding.postVideoView.setOnClickListener {
//                listener.onShareVideoClicked(recipe)
//            }
//            binding.postTextContent.setOnClickListener {
//                listener.onPostContentClicked(
//                    recipe
//                )
//            }
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding)
            {
                recipeName.text = recipe.name
                recipeAuthor.text = recipe.author
                recipeCategory.text = recipe.category.value
                recipeCardHeart.isChecked = recipe.likedByMe
                recipeCardHeart.text = valueToStringForShowing(recipe.likes)
//                postAvatar.setImageResource(
//                    when (recipe.author) {
//                        "Нетология. Университет интернет-профессий" ->
//                            R.drawable.ic_launcher_foreground
//                        "Skillbox. Образовательная платформа" ->
//                            R.drawable.ic_skillbox
//                        else ->
//                            R.drawable.ic_baseline_tag_faces_24
//                    }
//                )
//                postAuthor.text = recipe.author
//                postPublished.text = recipe.published
//                postTextContent.text = recipe.ingredients
//                fabVideo.visibility = if (recipe.videoContent != null) {
//                    View.VISIBLE
//                } else View.GONE
//                postVideoView.visibility = if (recipe.videoContent != null) {
//                    View.VISIBLE
//                } else View.GONE
//                views.text = valueToStringForShowing(
//                    when (recipe.author) {
//                        "Нетология. Университет интернет-профессий" ->
//                            2999999
//                        "Skillbox. Образовательная платформа" ->
//                            999
//                        else ->
//                            0
//                    }
//                )
//                postHeart.text = valueToStringForShowing(recipe.likes)
//                postHeart.isChecked = recipe.likedByMe
//                share.text = valueToStringForShowing(recipe.shared)
//                share.isChecked = recipe.sharedByMe
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Recipe>() {

        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
           oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem
    }
}
