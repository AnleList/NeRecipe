package ru.netology.nerecipe.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.data.Post
import ru.netology.nerecipe.data.PostRepository
import ru.netology.nerecipe.db.PostDao
import ru.netology.nerecipe.db.toEntity
import ru.netology.nerecipe.db.toModel

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map {entities ->
        entities.map { it.toModel() }
    }

    init {
        if (!dao.hasAnyPosts()) {
            val postNetology = Post(
                id = 0L,
                author = "Нетология. Университет интернет-профессий",
                textContent = "Помогаем расти в профессии и открывать новые карьерные горизонты \uD83D\uDCAB\n" +
                        "\n" +
                        "Нетология — это четыре уровня образования:\n" +
                        "\n" +
                        "\uD83D\uDD39 Для начинающих, которые хотят освоить актуальную профессию с нуля\n" +
                        "\uD83D\uDD39 Для специалистов, которые стремятся вырасти в карьере\n" +
                        "\uD83D\uDD39 Для руководителей, которые нацелены усилить лидерские навыки\n" +
                        "\uD83D\uDD39 И для компаний, которым нужно обучить команду\n" +
                        "\n" +
                        "Маркетинг, дизайн, программирование, soft skills, MBA, управление проектами, аналитика и Data Science — выбирайте, что вам ближе и приходите к нам за знаниями и ростом.\n" +
                        "\n" +
                        "Перемены — всегда правильный выбор \uD83D\uDC4C\n" +
                        "http://netolo.gy/fEU\n" +
                        "8 (800) 301-39-69\n" +
                        "Варшавское шоссе, д. 1, стр. 6, 1 этаж, офис 105А, Москва",
                draftTextContent = null,
                videoContent = "https://youtu.be/xOgT2qYAzds",
                published = "21 мая 2020",
                likedByMe = true,
                likes = 1100,
                sharedByMe = false,
                shared = 999997,
            )
            val postSkillbox = Post(
                id = 0L,
                author = "Skillbox. Образовательная платформа",
                textContent = "Skillbox — образовательная платформа, которая объединяет ведущих экспертов и практиков рынка, методистов и продюсеров образовательного контента.\n" +
                        "\n" +
                        "Skillbox лидер сегмента дополнительного профессионального онлайн-образования согласно данным исследования “Интерфакс Академии” в 2020 году, лидер рейтинга РБК EdTech-компаний за второй квартал 2020 года, победитель премии Рунета в номинациях “Образование и кадры” (2018, 2020) и “Технологии и инновации” (2019).\n" +
                        "https://vk.cc/9OuvQO\n" +
                        "8 (800) 500-05-22\n" +
                        "Обучающие курсы\n" +
                        "Ленинский проспект, дом 6, строение 20, Москва",
                draftTextContent = null,
                videoContent = "https://youtu.be/Q0KL76IK4_0",
                published = "1 ноя 2021",
                likedByMe = false,
                likes = 99,
                sharedByMe = false,
                shared = 997,
            )
            val postAny = Post(
                id = 0L,
                author = "Некая образовательная организация",
                textContent = "Тут информация об этой организации",
                draftTextContent = null,
                videoContent = null,
                published = "ДД ммм ГГГГ",
                likedByMe = false,
                likes = 0,
                sharedByMe = false,
                shared = 0,
            )
            dao.insert(postNetology.toEntity())
            dao.insert(postSkillbox.toEntity())
            dao.insert(postAny.toEntity())
        }
    }

    override fun save(post: Post) {
        dao.save(post.toEntity())
    }

    override fun likeById(postId: Long) {
        dao.likeById(postId)
    }

    override fun shareBiId(postId: Long) {
        dao.shareBiId(postId)
    }

    override fun removeById(postId: Long) {
        dao.removeById(postId)
    }

    override fun getAll(): LiveData<List<Post>> = data
}
