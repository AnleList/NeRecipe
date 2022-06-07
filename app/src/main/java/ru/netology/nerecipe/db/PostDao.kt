package ru.netology.nerecipe.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage

@Dao
interface PostDao {
    @Query("SELECT * FROM recipes WHERE recipeCategory LIKE :ink")
    fun getAll(ink: String): LiveData<List<PostEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getById(id: Long): PostEntity

    @Insert
    fun insert(post: PostEntity)

    @Update()
    fun update(post: PostEntity)

//    @Query("""UPDATE recipes SET
//        author = :author,
//        recipeName = :name,
//        recipeCategory = :name,
//        ingredients = :ingredients,
//        stages = :stages,
//        draftTextContent = :draftTextContent,
//        videoContent = :videoContent,
//        published = :published,
//        likedByMe = :likedByMe,
//        likes = :likes,
//        sharedByMe = :sharedByMe,
//        shared = :shared
//        WHERE id = :id""")
//    fun updateRecipeById(
//         id: Long,
//         author: String,
//         name: String,
//         category: RecipeCategories,
//         ingredients: String,
//         stages: List<Stage>?,
//         draftTextContent: String?,
//         videoContent: String?,
//         published: String,
//         likedByMe: Boolean,
//         likes: Int,
//         sharedByMe: Boolean,
//         shared: Int,
//    )

    @Query("UPDATE recipes SET recipeName = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Query("UPDATE recipes SET draftTextContent = :draft WHERE id = :id")
    fun updateDraftById(id: Long, draft: String)

    fun save(post: PostEntity) =
        if (post.id == 0L)
            insert(post)
        else if (post.draftTextContent != null) {
            updateDraftById(post.id, post.draftTextContent)
        } else
            updateContentById(post.id, post.recipeName)

    @Query(
        """
            UPDATE recipes SET
            likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
            likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)

    @Query(
        """
        UPDATE recipes SET
        sharedByMe = CASE WHEN sharedByMe THEN 0 ELSE 1 END,
        shared = shared + 1
        WHERE id = :id
        """
    )
    fun shareBiId(id: Long)

    @Query("SELECT COUNT(id) FROM recipes LIMIT 1")
    fun hasAnyRecipes(): Boolean
}