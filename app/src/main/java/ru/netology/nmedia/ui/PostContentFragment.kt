package ru.netology.nmedia.ui

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostContentFragmentBinding
import ru.netology.nmedia.util.showKeyboard

class PostContentFragment : Fragment( ) {

//    private val initialContent by lazy {
        private val args by navArgs<PostContentFragmentArgs>()
//        args.initialContent
//    }

//        get() = requireArguments().getString(TEXT_TO_EDIT_KEY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        with(binding.edit) {
            setText(args.initialContent)
            requestFocus()
            setSelection(binding.edit.text.length)
//            showKeyboard()
            showSoftInputOnFocus
        }
        binding.ok.setOnClickListener {
            binding.onSaveButtonClicked()
        }
    }.root

    private fun PostContentFragmentBinding.onSaveButtonClicked() {

        val textToSave = edit.text

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
//        const val TEXT_TO_EDIT_KEY = "ru.netology.nmedia.PostContent.TEXT_TO_EDIT"
//
//        fun create(initialContent: String?) = PostContentFragment().apply {
//            arguments = createBundle(initialContent)
//        }
//
//        fun createBundle(initialContent: String?)  = Bundle(1).apply {
//            putString(TEXT_TO_EDIT_KEY, initialContent)
//        }
    }
}