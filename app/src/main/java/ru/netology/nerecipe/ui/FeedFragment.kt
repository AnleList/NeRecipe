package ru.netology.nerecipe.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.adapters.RecipesAdapter
import ru.netology.nerecipe.databinding.FeedFragmetBinding
import ru.netology.nerecipe.view_models.RecipeViewModel


class FeedFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel.sharePostVideo.observe(this) { postVideoContent ->
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(postVideoContent))
//            val shareIntent = Intent.createChooser(
//                intent, getString(R.string.chooser_share_post_video)
//            )
//            startActivity(shareIntent)
//        }
//
//        viewModel.sharePostContent.observe(this) { postContent ->
//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, postContent)
//                type = "text/plain"
//            }
//
//            val shareIntent = Intent.createChooser(
//                intent, getString(R.string.chooser_share_post)
//            )
//            startActivity(shareIntent)
//        }
//
//        viewModel.navToPostEditContentEvent.observe(this) { postContent ->
//            val direction
//                = FeedFragmentDirections.actionFeedFragmentToPostContentFragment(postContent)
//            findNavController().navigate(direction)
//        }

        viewModel.navToRecipeViewing.observe(this) { recipe ->
            val direction
                = recipe?.let {
                FeedFragmentDirections.actionFeedFragmentToPostViewingFragment(it)
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

        val adapter = RecipesAdapter(viewModel)
        binding.recipesRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {recipes ->
            adapter.submitList(recipes)
        }

        binding.filterByName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                filterText = s.toString()
                viewModel.inFilterChange(filterText)
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }
    }.root

    companion object {
        const val TAG = "FeedFragment"
    }

}
