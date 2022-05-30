package ru.netology.nerecipe.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nerecipe.adapters.PostInteractionListener
import ru.netology.nerecipe.data.Post
import ru.netology.nerecipe.data.PostRepository
import ru.netology.nerecipe.data.impl.PostRepositoryImpl
import ru.netology.nerecipe.db.AppDb
import ru.netology.nerecipe.util.SingleLiveEvent
import java.text.SimpleDateFormat
import java.util.*

class PostViewModel(
    application: Application
): AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navToPostViewing = SingleLiveEvent<Post?>()
    val navToPostEditContentEvent = SingleLiveEvent<String>()
    private val navToFeedFragment = SingleLiveEvent<Unit>()
    private val currentPost = MutableLiveData<Post?>(null)
    val sharePostVideo = SingleLiveEvent<String?>()

    fun onEditBackPressed(draft: String){
        if (currentPost.value != null) {
            currentPost.value?.copy(
                draftTextContent = draft
            )?.let {
                repository.save(it)
            }
            currentPost.value = null
        }
    }

    fun onSaveClicked(content: String) {
        if (content.isBlank()) return
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("LOCALIZE"))
        val postToAdd = currentPost.value?.copy(
            textContent = content,
            draftTextContent = null
            ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "New author",
            textContent = content,
            draftTextContent = null,
            videoContent = null,
            published = (sdf.format(Date())).toString()
        )
        repository.save(postToAdd)
        currentPost.value = null
    }

    override fun onShareVideoClicked(post: Post) {
        sharePostVideo.value = post.videoContent
    }

    override fun onHeartClicked(post: Post) =
        repository.likeById(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.textContent
        repository.shareBiId(post.id)
    }

    override fun onRemoveClicked(post: Post) {
        repository.removeById(post.id)
        navToFeedFragment.call()
    }

    override fun onEditClicked(post: Post) {
        navToPostEditContentEvent.value =
            post.draftTextContent ?: post.textContent
        currentPost.value = post
    }

    override fun onUnDoClicked() {
        currentPost.value = null
    }

    override fun onAddClicked() {
      navToPostEditContentEvent.call()
    }

    override fun onPostContentClicked(post: Post) {
        navToPostViewing.value = post
    }
}