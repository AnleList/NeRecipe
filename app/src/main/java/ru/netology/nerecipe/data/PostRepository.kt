package ru.netology.nerecipe.data

import androidx.lifecycle.LiveData

interface PostRepository {

    val data: LiveData<List<Recipe>>
    var filter: String?


    fun getAll(): LiveData<List<Recipe>>
    fun likeById(postId: Long)
    fun shareBiId(postId: Long)
    fun removeById(postId: Long)
    fun save(recipe: Recipe)
    fun changeFilter(filter: String)

    companion object {
        const val NEW_POST_ID = 0L
    }
}