package ru.netology.nerecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.databinding.RecipeCardLayoutBinding
import ru.netology.nerecipe.valueToStringForShowing

internal class FavoriteRecipesAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, FavoriteRecipesAdapter.ViewHolder>(DiffCallback) {

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

        init {
            binding.recipeCardHeart.setOnClickListener {
                listener.onHeartClicked(recipe)
            }
            binding.recipeCardMenuButton.isVisible = false
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
