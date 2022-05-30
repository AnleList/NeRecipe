package ru.netology.nerecipe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nerecipe.R
import ru.netology.nerecipe.data.Post
import ru.netology.nerecipe.databinding.PostCardLayoutBinding
import ru.netology.nerecipe.valueToStringForShowing

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostCardLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder (
        private val binding: PostCardLayoutBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.postMenuButton).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.removeItem -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.editItem -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.postHeart.setOnClickListener {
                listener.onHeartClicked(post)
            }
            binding.share.setOnClickListener {
                listener.onShareClicked(post)
            }
            binding.fabVideo.setOnClickListener {
                listener.onShareVideoClicked(post)
            }
            binding.postVideoView.setOnClickListener {
                listener.onShareVideoClicked(post)
            }
            binding.postMenuButton.setOnClickListener {
                popupMenu.show()
            }
            binding.postTextContent.setOnClickListener {
                listener.onPostContentClicked(
                    post
                )
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding)
            {
                postAvatar.setImageResource(
                    when (post.author) {
                        "Нетология. Университет интернет-профессий" ->
                            R.drawable.ic_launcher_foreground
                        "Skillbox. Образовательная платформа" ->
                            R.drawable.ic_skillbox
                        else ->
                            R.drawable.ic_baseline_tag_faces_24
                    }
                )
                postAuthor.text = post.author
                postPublished.text = post.published
                postTextContent.text = post.textContent
                fabVideo.visibility = if (post.videoContent != null) {
                    View.VISIBLE
                } else View.GONE
                postVideoView.visibility = if (post.videoContent != null) {
                    View.VISIBLE
                } else View.GONE
                views.text = valueToStringForShowing(
                    when (post.author) {
                        "Нетология. Университет интернет-профессий" ->
                            2999999
                        "Skillbox. Образовательная платформа" ->
                            999
                        else ->
                            0
                    }
                )
                postHeart.text = valueToStringForShowing(post.likes)
                postHeart.isChecked = post.likedByMe
                share.text = valueToStringForShowing(post.shared)
                share.isChecked = post.sharedByMe
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
           oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}
