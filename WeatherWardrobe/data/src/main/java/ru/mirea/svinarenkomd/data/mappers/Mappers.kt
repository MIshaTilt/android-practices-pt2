package ru.mirea.svinarenkomd.data.mappers

import ru.mirea.svinarenkomd.data.network.models.ClothingDto
import ru.mirea.svinarenkomd.data.network.models.WeatherDto
import ru.mirea.svinarenkomd.data.storage.models.ClothingEntity
import ru.mirea.svinarenkomd.data.storage.models.UserPrefsModel
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.models.UserProfile
import ru.mirea.svinarenkomd.domain.models.WeatherInfo
import java.time.LocalDate

fun ClothingItem.toEntity(): ClothingEntity = ClothingEntity(
    id = id,
    name = name,
    category = category,
    minTemp = minTemp,
    maxTemp = maxTemp,
    imageUrl = imageUrl,
    description = description,
    savedDate = LocalDate.now().toString()
)

fun ClothingEntity.toDomain(): ClothingItem = ClothingItem(
    id = id,
    name = name,
    category = category,
    minTemp = minTemp,
    maxTemp = maxTemp,
    imageUrl = imageUrl,
    description = description,
    isFavorite = true
)

fun ClothingDto.toDomain(isFavorite: Boolean = false): ClothingItem = ClothingItem(
    id = id,
    name = name,
    category = category,
    minTemp = minTemp,
    maxTemp = maxTemp,
    imageUrl = imageUrl,
    description = description,
    isFavorite = isFavorite
)

fun WeatherDto.toDomain(recommendation: String): WeatherInfo = WeatherInfo(
    city = city,
    temperature = temperature,
    condition = condition,
    windSpeed = windSpeed,
    recommendation = recommendation
)

fun UserPrefsModel.toDomain(): UserProfile = UserProfile(
    email = email,
    preferredCity = preferredCity,
    isAuthorized = isAuthorized
)
