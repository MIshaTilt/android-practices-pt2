package ru.mirea.svinarenkomd.data.network

import ru.mirea.svinarenkomd.data.network.models.ClothingDto
import ru.mirea.svinarenkomd.data.network.models.WeatherDto

class NetworkApi {

    private val mockCatalog = listOf(
        ClothingDto(
            id = 1,
            name = "Мембранная ветровка Urban",
            category = "Верхняя одежда (Плащ / Пальто / Ветровка)",
            minTemp = 8,
            maxTemp = 18,
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600",
            description = "Лёгкая непродуваемая куртка с водоотталкивающей пропиткой для прохладной и ветреной погоды."
        ),
        ClothingDto(
            id = 2,
            name = "Оверсайз худи с начёсом",
            category = "Верх (Худи / Толстовка)",
            minTemp = 8,
            maxTemp = 18,
            imageUrl = "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600",
            description = "Мягкое хлопковое худи для прохладных весенних и осенних дней."
        ),
        ClothingDto(
            id = 3,
            name = "Классические синие джинсы",
            category = "Низ (Джинсы / Брюки)",
            minTemp = -5,
            maxTemp = 22,
            imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?w=600",
            description = "Прямые джинсы из плотного денима на каждый день."
        ),
        ClothingDto(
            id = 4,
            name = "Кожаные кеды Street",
            category = "Обувь",
            minTemp = 5,
            maxTemp = 25,
            imageUrl = "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600",
            description = "Удобные демисезонные кеды с защитой от влаги."
        ),
        ClothingDto(
            id = 5,
            name = "Зимний пуховик Arctic",
            category = "Верхняя одежда (Зимняя куртка / Пуховик)",
            minTemp = -25,
            maxTemp = 0,
            imageUrl = "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=600",
            description = "Тёплый зимний пуховик с капюшоном для морозной погоды."
        ),
        ClothingDto(
            id = 6,
            name = "Базовая белая футболка",
            category = "Верх (Футболка)",
            minTemp = 20,
            maxTemp = 35,
            imageUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600",
            description = "Дышащая футболка из 100% хлопка для жаркой погоды."
        )
    )

    fun fetchClothingCatalog(): List<ClothingDto> {
        return mockCatalog
    }

    fun fetchWeather(city: String): WeatherDto {
        return when (city.trim().lowercase()) {
            "сочи" -> WeatherDto(
                city = "Сочи",
                temperature = 23,
                condition = "Ясно, солнечно",
                windSpeed = 3
            )
            "новосибирск", "мурманск" -> WeatherDto(
                city = city.trim(),
                temperature = -8,
                condition = "Снег, морозно",
                windSpeed = 6
            )
            "санкт-петербург", "спб", "питер" -> WeatherDto(
                city = "Санкт-Петербург",
                temperature = 11,
                condition = "Облачно, небольшой дождь",
                windSpeed = 7
            )
            else -> WeatherDto(
                city = city.trim().ifEmpty { "Москва" },
                temperature = 14,
                condition = "Переменная облачность",
                windSpeed = 4
            )
        }
    }
}
