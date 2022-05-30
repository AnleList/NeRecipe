package ru.netology.nerecipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nerecipe.R
import ru.netology.nerecipe.adapters.PostsAdapter
import ru.netology.nerecipe.databinding.FeedFragmetBinding
import ru.netology.nerecipe.view_models.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel by activityViewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostVideo.observe(this) { postVideoContent ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(postVideoContent))
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post_video)
            )
            startActivity(shareIntent)
        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.navToPostEditContentEvent.observe(this) { postContent ->
            val direction
                = FeedFragmentDirections.actionFeedFragmentToPostContentFragment(postContent)
            findNavController().navigate(direction)
        }

        viewModel.navToPostViewing.observe(this) { post ->
            val direction
                = post?.let {
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
        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }
    }.root

    companion object {
        const val TAG = "FeedFragment"
    }

}
