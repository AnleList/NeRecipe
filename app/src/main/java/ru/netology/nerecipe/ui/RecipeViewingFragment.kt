package ru.netology.nerecipe.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapters.StagesAdapter
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.databinding.RecipeViewingFragmentBinding
import ru.netology.nerecipe.valueToStringForShowing
import ru.netology.nerecipe.view_models.RecipeViewModel


class RecipeViewingFragment : Fragment() {

    private val args by navArgs<RecipeViewingFragmentArgs>()
    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navToRecipeEdit.observe(this) { recipe ->
            val direction = RecipeViewingFragmentDirections
                .actionRecipeViewingFragmentToRecipeContentFragment(recipe)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeViewingFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val adapter = StagesAdapter(viewModel)
        binding.stagesRecyclerView.adapter = adapter
        viewModel.currentRecipe.observe(viewLifecycleOwner) {recipe ->
            if (recipe != null) {
                adapter.submitList(recipe.stages)
            }
        }
        val callback = object :
            ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
                }
            }
            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.setBackgroundColor(0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                viewModel.moveStage(fromPosition, toPosition)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.END) {
                    val position = viewHolder.adapterPosition
                    viewModel.deleteStage(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.stagesRecyclerView)

        var recipeToViewing: Recipe = args.recipeToViewing

        viewModel.data.observe(viewLifecycleOwner) {recipes ->
            recipeToViewing = recipes.first {it.id == recipeToViewing.id}
            with(binding) {
                recipeName.text = recipeToViewing.name
                recipeAuthor.text = recipeToViewing.author
                recipeCategory.text = recipeToViewing.category.value
                viewingHeart.text = valueToStringForShowing(recipeToViewing.likes)
                viewingHeart.isChecked = recipeToViewing.likedByMe
                viewingHeart.setOnClickListener { viewModel.onHeartClicked(recipeToViewing) }
            }
            val popupMenu by lazy {
                PopupMenu(requireContext(), binding.feedFragmentMenuButton).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.removeItem -> {
                                viewModel.removeRecipeById(recipeToViewing.id)
                                findNavController().popBackStack()
                                true
                            }
                            R.id.editItem -> {
                                viewModel.editRecipe(recipeToViewing)
                                true
                            }
                            else -> false
                        }
                    }
                }
            }
            binding.feedFragmentMenuButton.setOnClickListener {
                popupMenu.show()
            }
        }
    }.root
}