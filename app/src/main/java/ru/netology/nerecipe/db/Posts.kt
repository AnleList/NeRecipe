package ru.netology.nerecipe.db

import ru.netology.nerecipe.data.Recipe

internal fun PostEntity.toModel(): Recipe {

    return Recipe(
        id = id,
        author = author,
        name = recipeName,
        category = recipeCategory,
        ingredients = ingredients,
        stages = stages,
        draftTextContent = draftTextContent,
        published = published,
        videoContent = videoContent,
        likes = likes,
        likedByMe = likedByMe,
        shared = shared,
        sharedByMe = sharedByMe
    )
}

internal fun Recipe.toEntity(): PostEntity {

    return PostEntity(
        id = id,
        author = author,
        recipeName = name,
        recipeCategory = category,
        ingredients = ingredients,
        stages = stages,
        draftTextContent = draftTextContent,
        published = published,
        videoContent = videoContent,
        likes = likes,
        likedByMe = likedByMe,
        shared = shared,
        sharedByMe = sharedByMe
    )
}