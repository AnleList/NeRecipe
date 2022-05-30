package ru.netology.nerecipe.adapters

import ru.netology.nerecipe.data.Post

interface PostInteractionListener {
    fun onHeartClicked(post: Post)
    fun onShareClicked(post: Post)
    fun onRemoveClicked(post: Post)
    fun onEditClicked(post: Post)
    fun onUnDoClicked()
    fun onAddClicked()
    fun onShareVideoClicked(post: Post)
    fun onPostContentClicked(post: Post)
}