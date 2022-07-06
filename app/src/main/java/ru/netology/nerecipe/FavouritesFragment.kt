package ru.netology.nerecipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.adapters.FavoriteRecipesAdapter
import ru.netology.nerecipe.adapters.RecipesAdapter
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.databinding.FeedFragmetBinding
import ru.netology.nerecipe.ui.FeedFragmentDirections
import ru.netology.nerecipe.view_models.RecipeViewModel

class FavouritesFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navToRecipeEdit.observe(this) { recipe ->
            val direction
                    = recipe?.let {
                FeedFragmentDirections.actionFeedFragmentToRecipeContentFragment(recipe)
            }
            if (direction != null) {
                findNavController().navigate(direction)
            }
        }

        viewModel.navToRecipeViewing.observe(this) { recipe ->
            val direction
                    = recipe?.let {
                FeedFragmentDirections.actionFeedFragmentToRecipeViewingFragment(it)
            }
            if (direction != null) {
                findNavController().navigate(direction)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmetBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        var filterText: String

        val adapter = FavoriteRecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {recipes ->
            adapter.submitList(recipes)
        }

        val filterMenu by lazy {
            PopupMenu(context, binding.filterMenuButton).apply {
                inflate(R.menu.category_filter)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.categoryEuropean -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.European)
                            false
                        }
                        R.id.categoryEastern -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Eastern)
                            false
                        }
                        R.id.categoryOther -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Other)
                            false
                        }
                        R.id.categoryPanasian -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Panasian)
                            false
                        }
                        R.id.categoryAsian -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Asian)
                            false
                        }
                        R.id.categoryMediterranean -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Mediterranean)
                            false
                        }
                        R.id.categoryRussian -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.Russian)
                            false
                        }
                        R.id.categoryAmerican -> {
                            menuItem.isChecked = !menuItem.isChecked
                            viewModel.inFilterByCategoryChange(RecipeCategories.American)
                            false
                        }
                        else -> false
                    }
                }
            }
        }

        binding.filterByName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                filterText = s.toString()
                viewModel.inFilterByNameChange(filterText)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.fab.visibility = View.INVISIBLE
        binding.filterMenuButton.setOnClickListener {
            filterMenu.show()
        }
    }.root
}