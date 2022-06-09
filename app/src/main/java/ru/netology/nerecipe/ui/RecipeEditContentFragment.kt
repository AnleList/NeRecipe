package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapters.StagesAdapter
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage
import ru.netology.nerecipe.databinding.RecipeEditContentFragmentBinding
import ru.netology.nerecipe.util.showKeyboard
import ru.netology.nerecipe.view_models.RecipeViewModel


class RecipeEditContentFragment : Fragment() {

    private val args by navArgs<RecipeEditContentFragmentArgs>()
    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeEditContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        val adapter = StagesAdapter(viewModel)
        binding.stagesRecyclerView.adapter = adapter
        viewModel.navToRecipeEditContentEvent.observe(viewLifecycleOwner) {recipe ->
            adapter.submitList(recipe.stages)
        }

        val categoryPopupMenu by lazy {
            PopupMenu(context, binding.categoryMenuButton).apply {
                inflate(R.menu.options_category)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.categoryAmerican -> {
                            binding.category.text = RecipeCategories.American.value
                            true
                        }
                        R.id.categoryRussian -> {
                            binding.category.text = RecipeCategories.Russian.value
                            true
                        }
                        R.id.categoryAsian -> {
                            binding.category.text = RecipeCategories.Asian.value
                            true
                        }
                        R.id.categoryPanasian -> {
                            binding.category.text = RecipeCategories.Panasian.value
                            true
                        }
                        R.id.categoryMediterranean -> {
                            binding.category.text = RecipeCategories.Mediterranean.value
                            true
                        }
                        R.id.categoryEuropean -> {
                            binding.category.text = RecipeCategories.European.value
                            true
                        }
                        R.id.categoryEastern -> {
                            binding.category.text = RecipeCategories.Eastern.value
                            true
                        }
                        R.id.categoryOther -> {
                            binding.category.text = RecipeCategories.Other.value
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        val recipeToEdit: Recipe? = args.initialContent
        with(binding) {
            if (recipeToEdit != null) {
                recipeName.setText(recipeToEdit.name)
                author.setText(recipeToEdit.author)
                category.setText(recipeToEdit.category.value)
                ingredients.setText(recipeToEdit.ingredients)
            }
            eddStageText.setOnFocusChangeListener() { _, hasFocus ->
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
//        val recipeToUpdate = if (args.initialContent != null) args.initialContent!!.copy(
//        ) else Recipe(
//            id = 0,
//            name = recipeName.text.toString(),
//            author = author.text.toString(),
//            category =
//        )

        val textToSave = eddStageText.text
        viewModel.onSaveClicked(textToSave.toString())
        if (!textToSave.isNullOrBlank()) {
            val answerBundle = Bundle(1)
            answerBundle.putString(RESULT_KEY, textToSave.toString())
            setFragmentResult(REQUEST_KEY, answerBundle)
        }
        findNavController().popBackStack()
    }


    private fun RecipeEditContentFragmentBinding.onSaveButtonLongClicked() {
        TODO("Not yet implemented")
    }

    companion object {
        const val REQUEST_KEY = "ru.netology.nmedia.PostContent.requestKey"
        const val RESULT_KEY = "ru.netology.nmedia.PostContent.postNewContent"
    }
}