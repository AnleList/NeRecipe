package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeEditContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val recipeToEdit: Recipe = args.initialContent

        val adapter = StagesAdapter(viewModel)
        binding.stagesRecyclerView.adapter = adapter
        viewModel.navToRecipeEditContentEvent.observe(viewLifecycleOwner) {recipe ->
            if (recipe != null) {
                adapter.submitList(recipe.stages)
            }
        }

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
                if (hasFocus)
                    binding.stagesRecyclerView.smoothScrollToPosition(
                        adapter.itemCount - 1
                    )
            }
        }

//        val callback: OnBackPressedCallback =
//            object : OnBackPressedCallback(true /* enabled by default */) {
//                override fun handleOnBackPressed() {
//                    viewModel.onEditBackPressed(
//                        binding.edit.text.toString()
//                    )
//                    findNavController().popBackStack()
//                }
//            }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.save.setOnClickListener {
            nextStageId = adapter.itemCount + 1
            binding.onSaveButtonClicked()
        }
        binding.save.setOnLongClickListener {
            binding.onSaveButtonLongClicked()
            return@setOnLongClickListener true
        }
        binding.categoryMenuButton.setOnClickListener {
            categoryPopupMenu.show()
        }
    }.root

    private fun RecipeEditContentFragmentBinding.onSaveButtonClicked() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("LOCALIZE"))
        val stageToAdd = Stage(
            id = nextStageId,
            text = addStageText.text.toString(),
            imageURL = addStageUrl.text.toString()
            )
        var stages: MutableList<Stage> = args.initialContent.stages.toMutableList()
        stages.add(stageToAdd)
        val recipeToUpdate = if (args.initialContent.id != 0L)
            args.initialContent.copy(
                name = recipeName.text.toString(),
                author = author.text.toString(),
                category = selectedCategory,
                ingredients = ingredients.text.toString(),
                stages = stages
            )
            else args.initialContent.copy(
                id = 0,
                name = recipeName.text.toString(),
                author = author.text.toString(),
                category = selectedCategory,
                ingredients = ingredients.text.toString(),
                published = (sdf.format(Date())).toString(),
                stages = stages
            )
        if (recipeName.text.isNullOrBlank()) {
            Toast.makeText(activity, "message...", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            viewModel.onSaveClicked(recipeToUpdate)
            viewModel.onEditClicked(recipeToUpdate)
        }

//        if (recipeToUpdate != null) {
//            val answerBundle = Bundle(1)
//            answerBundle.putString(RESULT_KEY, recipeToUpdate)
//            setFragmentResult(REQUEST_KEY, answerBundle)
//        }
    }


    private fun RecipeEditContentFragmentBinding.onSaveButtonLongClicked() {
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "ru.netology.nmedia.PostContent.requestKey"
        const val RESULT_KEY = "ru.netology.nmedia.PostContent.postNewContent"
    }
}