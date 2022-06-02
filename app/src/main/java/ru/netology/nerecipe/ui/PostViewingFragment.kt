package ru.netology.nerecipe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.databinding.PostViewingFragmentBinding
import ru.netology.nerecipe.valueToStringForShowing
import ru.netology.nerecipe.view_models.RecipeViewModel

class PostViewingFragment : Fragment() {

    private val args by navArgs<PostViewingFragmentArgs>()
    private val viewModel by activityViewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.navToPostEditContentEvent.observe(this) { postContent ->
            val direction = PostViewingFragmentDirections
                .actionPostViewingFragmentToPostContentFragment(postContent)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostViewingFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->

        var recipeToViewing: Recipe = args.postToViewing

        viewModel.data.observe(viewLifecycleOwner) {recipes ->
            recipeToViewing = recipes.first {it.id == recipeToViewing.id}
//            with(binding.includedRecipe) {
//                postAvatar.setImageResource(
//                    when (recipeToViewing.author) {
//                        "Нетология. Университет интернет-профессий" ->
//                            R.drawable.ic_launcher_foreground
//                        "Skillbox. Образовательная платформа" ->
//                            R.drawable.ic_skillbox
//                        else ->
//                            R.drawable.ic_baseline_tag_faces_24
//                    }
//                )
//                postAuthor.text = recipeToViewing.author
//                postPublished.text = recipeToViewing.published
//                postTextContent.text = recipeToViewing.ingredients
//                fabVideo.visibility = if (recipeToViewing.videoContent != null) {
//                    View.VISIBLE
//                } else View.GONE
//                postVideoView.visibility = if (recipeToViewing.videoContent != null) {
//                    View.VISIBLE
//                } else View.GONE
//                views.text = valueToStringForShowing(
//                    when (recipeToViewing.author) {
//                        "Нетология. Университет интернет-профессий" ->
//                            2999999
//                        "Skillbox. Образовательная платформа" ->
//                            999
//                        else ->
//                            0
//                    }
//                )
//                postHeart.text = valueToStringForShowing(recipeToViewing.likes)
//                postHeart.isChecked = recipeToViewing.likedByMe
//                share.text = valueToStringForShowing(recipeToViewing.shared)
//                share.isChecked = recipeToViewing.sharedByMe
//                postHeart.setOnClickListener { viewModel.onHeartClicked(recipeToViewing) }
//            }
//            val popupMenu by lazy {
//                PopupMenu(requireContext(), binding.includedRecipe.postMenuButton).apply {
//                    inflate(R.menu.options_post)
//                    setOnMenuItemClickListener { menuItem ->
//                        when (menuItem.itemId) {
//                            R.id.removeItem -> {
//                                viewModel.onRemoveClicked(recipeToViewing)
//                                findNavController().popBackStack()
//                                true
//                            }
//                            R.id.editItem -> {
//                                viewModel.onEditClicked(recipeToViewing)
//                                true
//                            }
//                            else -> false
//                        }
//                    }
//                }
//            }
//            binding.includedRecipe.postMenuButton.setOnClickListener {
//                popupMenu.show()
//            }
//
//            binding.includedRecipe.postHeart.setOnClickListener {
//                viewModel.onHeartClicked(recipeToViewing)
//            }
//            binding.includedRecipe.share.setOnClickListener {
//                viewModel.onShareClicked(recipeToViewing)
//            }
//            binding.includedRecipe.fabVideo.setOnClickListener {
//                viewModel.onShareVideoClicked(recipeToViewing)
//            }
//            binding.includedRecipe.postVideoView.setOnClickListener {
//                viewModel.onShareVideoClicked(recipeToViewing)
//            }
//            binding.includedRecipe.postTextContent.setOnClickListener {
//                viewModel.onEditClicked(recipeToViewing)
//            }
        }
//
//        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
//            val intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_TEXT, postContent)
//                type = "text/plain"
//            }
//            val shareIntent = Intent.createChooser(
//                intent, getString(R.string.chooser_share_post)
//            )
//            startActivity(shareIntent)
//        }
    }.root

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
}