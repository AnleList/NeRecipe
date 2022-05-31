package ru.netology.nerecipe.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.PostRepository
import ru.netology.nerecipe.data.RecipeCategories
import ru.netology.nerecipe.data.Stage
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
            val recipeNetology = Recipe(
                id = 0L,
                author = "www.RussianFood.com",
                recipeName = "Картофель по-французски.",
                recipeCategory = RecipeCategories.European,
                ingredients = "Ингредиенты:\n" +
                        "картофель \n" +
                        "\n" +
                        "1 кг\n" +
                        "\n" +
                        "\n" +
                        "сливки \n" +
                        "\n" +
                        "500 мл\n" +
                        "\n" +
                        "или молоко \n" +
                        "\n" +
                        "500 мл\n" +
                        "\n" +
                        "\n" +
                        "сыр твёрдый \n" +
                        "\n" +
                        "100 г\n" +
                        "\n" +
                        "\n" +
                        "масло сливочное \n" +
                        "\n" +
                        "50 г\n" +
                        "\n" +
                        "\n" +
                        "чеснок \n" +
                        "\n" +
                        "2-3 зубчика\n" +
                        "\n" +
                        "\n" +
                        "соль \n" +
                        "\n" +
                        "2 ч. ложки\n" +
                        "\n" +
                        "\n" +
                        "перец чёрный молотый \n" +
                        "\n" +
                        "0.5 ч. ложки\n" +
                        "\n" +
                        "\n" +
                        "орех мускатный \n" +
                        "\n" +
                        "1 щепотка\n",
                stages = listOf(Stage(1,
                    "Подготавливаем продукты для картофеля по-французски.",
                    "https://img1.russianfood.com/dycontent/images_upl/174/sm_173314.jpg\n"),
                    Stage(2,
                    "Как приготовить картофель по-французски:\n" +
                            "\n" +
                            "один килограмм картофеля чистим, моем и нарезаем пластинками, толщиной 2,5 миллиметра.",
                    "https://img1.russianfood.com/dycontent/images_upl/174/sm_173315.jpg\n"),
                    Stage(3,
                    "В кастрюлю наливаем 500 миллилитров сливок или молока (можно пополам), ставим на огонь. Добавляем 2 чайные ложки без верха соли, 0,5 чайной ложки черного молотого перца, щепотку мускатного ореха, доводим до кипения.",
                    "https://img1.russianfood.com/dycontent/images_upl/174/big_173316.jpg\n")
                ),
                draftTextContent = null,
                videoContent = "https://youtu.be/xOgT2qYAzds",
                published = "21 мая 2020",
                likedByMe = true,
                likes = 1100,
                sharedByMe = false,
                shared = 999997,
            )
//            val recipeSkillbox = Recipe(
//                id = 0L,
//                author = "Skillbox. Образовательная платформа",
//                ingredients = "Skillbox — образовательная платформа, которая объединяет ведущих экспертов и практиков рынка, методистов и продюсеров образовательного контента.\n" +
//                        "\n" +
//                        "Skillbox лидер сегмента дополнительного профессионального онлайн-образования согласно данным исследования “Интерфакс Академии” в 2020 году, лидер рейтинга РБК EdTech-компаний за второй квартал 2020 года, победитель премии Рунета в номинациях “Образование и кадры” (2018, 2020) и “Технологии и инновации” (2019).\n" +
//                        "https://vk.cc/9OuvQO\n" +
//                        "8 (800) 500-05-22\n" +
//                        "Обучающие курсы\n" +
//                        "Ленинский проспект, дом 6, строение 20, Москва",
//                draftTextContent = null,
//                videoContent = "https://youtu.be/Q0KL76IK4_0",
//                published = "1 ноя 2021",
//                likedByMe = false,
//                likes = 99,
//                sharedByMe = false,
//                shared = 997,
//            )
//            val recipeAny = Recipe(
//                id = 0L,
//                author = "Некая образовательная организация",
//                ingredients = "Тут информация об этой организации",
//                draftTextContent = null,
//                videoContent = null,
//                published = "ДД ммм ГГГГ",
//                likedByMe = false,
//                likes = 0,
//                sharedByMe = false,
//                shared = 0,
//            )
            dao.insert(recipeNetology.toEntity())
//            dao.insert(recipeSkillbox.toEntity())
//            dao.insert(recipeAny.toEntity())
        }
    }

    override fun save(recipe: Recipe) {
        dao.save(recipe.toEntity())
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

    override fun getAll(): LiveData<List<Recipe>> = data
}
