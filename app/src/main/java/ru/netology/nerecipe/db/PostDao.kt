package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE posts SET textContent = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Query("UPDATE posts SET draftTextContent = :draft WHERE id = :id")
    fun updateDraftById(id: Long, draft: String)

    fun save(post: PostEntity) =
        if (post.id == 0L)
            insert(post)
        else if (post.draftTextContent != null) {
            updateDraftById(post.id, post.draftTextContent)
        } else
            updateContentById(post.id, post.textContent)

    @Query(
        """
            UPDATE posts SET
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")
    fun removeById(id: Long)

    @Query(
        """
        UPDATE posts SET
        sharedByMe = CASE WHEN sharedByMe THEN 0 ELSE 1 END,
        shared = shared + 1
        WHERE id = :id
        """
    )
    fun shareBiId(id: Long)

    @Query("SELECT COUNT(id) FROM posts LIMIT 1")
    fun hasAnyPosts(): Boolean
}