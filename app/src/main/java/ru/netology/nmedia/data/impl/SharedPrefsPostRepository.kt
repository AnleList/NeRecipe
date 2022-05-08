package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository

class SharedPrefsPostRepository(
    application: Application
) : PostRepository {

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private val data
    : MutableLiveData<List<Post>>
//    = MutableLiveData(
//        listOf(
//            Post(
//                id = 3L,
//                author = "Нетология. Университет интернет-профессий",
//                content = "Помогаем расти в профессии и открывать новые карьерные горизонты \uD83D\uDCAB\n" +
//                        "\n" +
//                        "Нетология — это четыре уровня образования:\n" +
//                        "\n" +
//                        "\uD83D\uDD39 Для начинающих, которые хотят освоить актуальную профессию с нуля\n" +
//                        "\uD83D\uDD39 Для специалистов, которые стремятся вырасти в карьере\n" +
//                        "\uD83D\uDD39 Для руководителей, которые нацелены усилить лидерские навыки\n" +
//                        "\uD83D\uDD39 И для компаний, которым нужно обучить команду\n" +
//                        "\n" +
//                        "Маркетинг, дизайн, программирование, soft skills, MBA, управление проектами, аналитика и Data Science — выбирайте, что вам ближе и приходите к нам за знаниями и ростом.\n" +
//                        "\n" +
//                        "Перемены — всегда правильный выбор \uD83D\uDC4C\n" +
//                        "http://netolo.gy/fEU\n" +
//                        "8 (800) 301-39-69\n" +
//                        "Варшавское шоссе, д. 1, стр. 6, 1 этаж, офис 105А, Москва",
//                videoContent = "https://youtu.be/xOgT2qYAzds",
//                published = "21 мая 2020",
//                likedByMe = true,
//                likes = 1100,
//                sharedByMe = false,
//                shared = 999997,
//            ),
//            Post(
//                id = 2L,
//                author = "Skillbox. Образовательная платформа",
//                content = "Skillbox — образовательная платформа, которая объединяет ведущих экспертов и практиков рынка, методистов и продюсеров образовательного контента.\n" +
//                        "\n" +
//                        "Skillbox лидер сегмента дополнительного профессионального онлайн-образования согласно данным исследования “Интерфакс Академии” в 2020 году, лидер рейтинга РБК EdTech-компаний за второй квартал 2020 года, победитель премии Рунета в номинациях “Образование и кадры” (2018, 2020) и “Технологии и инновации” (2019).\n" +
//                        "https://vk.cc/9OuvQO\n" +
//                        "8 (800) 500-05-22\n" +
//                        "Обучающие курсы\n" +
//                        "Ленинский проспект, дом 6, строение 20, Москва",
//                videoContent = "https://youtu.be/Q0KL76IK4_0",
//                published = "1 ноя 2021",
//                likedByMe = false,
//                likes = 99,
//                sharedByMe = false,
//                shared = 997,
//            ),
//            Post(
//                id = 1L,
//                author = "Некая образовательная организация",
//                content = "Тут информация об этой организации",
//                videoContent = null,
//                published = "ДД ммм ГГГГ",
//                likedByMe = false,
//                likes = 0,
//                sharedByMe = false,
//                shared = 0,
//            )
//        )
//    )

    init {
        val serializedPosts = prefs.getString(POSTS_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POSTS_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1,
                likedByMe = !it.likedByMe
            )
        }
    }

    override fun shareBiId(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                sharedByMe = true,
                shared = it.shared + 1
            )
        }
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID)
            insert(post)
        else update(post)
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post
            else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(
            id = if (posts.isEmpty()) 1L
            else (posts.first().id) + 1L
            )
        ) + posts
    }

    private companion object {
        const val POSTS_PREFS_KEY = "posts"
    }
}