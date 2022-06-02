package ru.netology.nerecipe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.databinding.PostEditContentFragmentBinding
import ru.netology.nerecipe.util.showKeyboard
import ru.netology.nerecipe.view_models.RecipeViewModel


class PostEditContentFragment : Fragment() {

        private val args by navArgs<PostEditContentFragmentArgs>()
        private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostEditContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        with(binding.edit) {
            setText(args.initialContent)
            setSelection(binding.edit.text.length)
            requestFocus()
            showSoftInputOnFocus
            showKeyboard()
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    viewModel.onEditBackPressed(
                        binding.edit.text.toString()
                    )
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



        binding.ok.setOnClickListener {
            binding.onSaveButtonClicked()
        }
    }.root

    private fun PostEditContentFragmentBinding.onSaveButtonClicked() {
        val textToSave = edit.text
        viewModel.onSaveClicked(textToSave.toString())
        if (!textToSave.isNullOrBlank()) {
            val answerBundle = Bundle(1)
            answerBundle.putString(RESULT_KEY, textToSave.toString())
            setFragmentResult(REQUEST_KEY, answerBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "ru.netology.nmedia.PostContent.requestKey"
        const val RESULT_KEY = "ru.netology.nmedia.PostContent.postNewContent"
    }
}