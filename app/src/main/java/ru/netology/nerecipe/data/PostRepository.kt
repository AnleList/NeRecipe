package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData

interface PostRepository {

    val data: LiveData<List<Post>>


    fun getAll(): LiveData<List<Post>>
    fun likeById(postId: Long)
    fun shareBiId(postId: Long)
    fun removeById(postId: Long)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }
}