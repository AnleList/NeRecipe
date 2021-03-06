package ru.netology.nerecipe.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapters.StagesAdapter
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage
import ru.netology.nerecipe.databinding.RecipeEditContentFragmentBinding
import ru.netology.nerecipe.view_models.RecipeViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class RecipeEditContentFragment : Fragment() {

    private val args by navArgs<RecipeEditContentFragmentArgs>()
    private val viewModel by activityViewModels<RecipeViewModel>()
    private lateinit var selectedCategory: RecipeCategories
    private var nextStageId by Delegates.notNull<Int>()
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("LOCALIZE"))
    private lateinit var recipeToEdit: Recipe

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeEditContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        recipeToEdit = args.initialContent

        val adapter = StagesAdapter(viewModel)
        binding.stagesRecyclerView.adapter = adapter
        viewModel.currentRecipe.observe(viewLifecycleOwner) {recipe ->
            if (recipe != null) {
                adapter.submitList(recipe.stages)
                recipeToEdit = recipeToEdit.copy(
                    stages = recipe.stages
                )
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

        val categoryPopupMenu by lazy {
            PopupMenu(context, binding.categoryMenuButton).apply {
                inflate(R.menu.options_category)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.categoryAmerican -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.American
                            true
                        }
                        R.id.categoryRussian -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Russian
                            true
                        }
                        R.id.categoryAsian -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Asian
                            true
                        }
                        R.id.categoryPanasian -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Panasian
                            true
                        }
                        R.id.categoryMediterranean -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Mediterranean
                            true
                        }
                        R.id.categoryEuropean -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.European
                            true
                        }
                        R.id.categoryEastern -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Eastern
                            true
                        }
                        R.id.categoryOther -> {
                            binding.category.text = menuItem.title
                            selectedCategory = RecipeCategories.Other
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        with(binding) {
            if (recipeToEdit.id != 0L) {
                selectedCategory = recipeToEdit.category
                recipeName.setText(recipeToEdit.name)
                author.setText(recipeToEdit.author)
                category.text = when (recipeToEdit.category) {
                    RecipeCategories.American -> getString(R.string.categoryAmerican)
                    RecipeCategories.Asian -> getString(R.string.categoryAsian)
                    RecipeCategories.Eastern -> getString(R.string.categoryEastern)
                    RecipeCategories.European -> getString(R.string.categoryEuropean)
                    RecipeCategories.Mediterranean -> getString(R.string.categoryMediterranean)
                    RecipeCategories.Other -> getString(R.string.categoryOther)
                    RecipeCategories.Panasian -> getString(R.string.categoryPanasian)
                    RecipeCategories.Russian -> getString(R.string.categoryRussian)
                }
                ingredients.setText(recipeToEdit.ingredients)
            }
            addStageText.setOnFocusChangeListener() { _, hasFocus ->
                if (hasFocus && !binding.stagesRecyclerView.isEmpty())
                    binding.stagesRecyclerView.smoothScrollToPosition(
                        adapter.itemCount - 1
                    )
            }
        }

        binding.save.setOnClickListener {
            nextStageId = adapter.itemCount + 1
            binding.saveRecipe()
            if (!(binding.addStageText.text.isNullOrBlank()
                && !binding.stagesRecyclerView.isEmpty())) {
                binding.addStageText.setText("")
                binding.addStageUrl.setText("")
                if (!binding.stagesRecyclerView.isEmpty())
                    binding.stagesRecyclerView.smoothScrollToPosition(
                        adapter.itemCount
                    )
            }
        }

        binding.categoryMenuButton.setOnClickListener {
            categoryPopupMenu.show()
        }
    }.root

    private fun RecipeEditContentFragmentBinding.saveRecipe() {
        val stages: MutableList<Stage> = recipeToEdit.stages.toMutableList()
        if (recipeName.text.isNullOrBlank()
            || author.text.isNullOrBlank()
            || category.text.isNullOrBlank()
            || ingredients.text.isNullOrBlank()) {
            Toast.makeText(activity, getString(R.string.notAllStar), Toast.LENGTH_LONG).show()
            return
        }
        else if (addStageText.text.isNullOrBlank() && stages.isEmpty()) {
            Toast.makeText(activity, getString(R.string.noCookingStep), Toast.LENGTH_LONG).show()
            return
        }
        else if (!addStageText.text.isNullOrBlank()) {
            val stageToAdd = Stage(
                id = nextStageId,
                text = addStageText.text.toString(),
                imageURL = addStageUrl.text.toString()
            )
            stages.add(stageToAdd)
            val recipeToEdit =
                if (recipeToEdit.id != 0L)
                    recipeToEdit.copy(
                        name = recipeName.text.toString(),
                        author = author.text.toString(),
                        category = selectedCategory,
                        ingredients = ingredients.text.toString(),
                        stages = stages
                    )
                else recipeToEdit.copy(
                    id = 0,
                    name = recipeName.text.toString(),
                    author = author.text.toString(),
                    category = selectedCategory,
                    ingredients = ingredients.text.toString(),
                    published = (sdf.format(Date())).toString(),
                    stages = stages
                )
            viewModel.editRecipe(recipeToEdit)
        }
        else if (addStageText.text.isNullOrBlank() && stages.isNotEmpty()) {
            val recipeToEdit =
                if (recipeToEdit.id != 0L)
                recipeToEdit.copy(
                    name = recipeName.text.toString(),
                    author = author.text.toString(),
                    category = selectedCategory,
                    ingredients = ingredients.text.toString(),
                    stages = stages
                )
                else recipeToEdit.copy(
                    id = 0,
                    name = recipeName.text.toString(),
                    author = author.text.toString(),
                    category = selectedCategory,
                    ingredients = ingredients.text.toString(),
                    published = (sdf.format(Date())).toString(),
                    stages = stages
                )
            viewModel.saveRecipe(recipeToEdit)
            viewModel.currentRecipe.value = null
//            findNavController().navigate(
//                RecipeEditContentFragmentDirections.actionRecipeEditFragmentToFeedFragment()
//            )
            findNavController().popBackStack()
        }
    }
}