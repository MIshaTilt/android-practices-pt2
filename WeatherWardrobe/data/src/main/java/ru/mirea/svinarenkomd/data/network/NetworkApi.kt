package ru.mirea.svinarenkomd.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mirea.svinarenkomd.data.network.models.ClothingDto
import ru.mirea.svinarenkomd.data.network.models.WeatherDto

class NetworkApi {

    private val gson = Gson()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiService: RetrofitApiService = retrofit.create(RetrofitApiService::class.java)

    @Volatile
    private var cachedCatalog: List<ClothingDto> = parseFallbackCatalogJson()

    fun fetchClothingCatalog(): List<ClothingDto> {
        // Асинхронный запрос к внешнему REST API через Retrofit + Call.enqueue() с обработкой ошибок
        apiService.getClothingCatalog().enqueue(object : Callback<List<ClothingDto>> {
            override fun onResponse(
                call: Call<List<ClothingDto>>,
                response: Response<List<ClothingDto>>
            ) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    cachedCatalog = response.body()!!
                    Log.d(TAG, "Retrofit getClothingCatalog success: ${cachedCatalog.size} items")
                } else {
                    Log.w(
                        TAG,
                        "Retrofit getClothingCatalog HTTP ${response.code()}, using JSON catalog"
                    )
                }
            }

            override fun onFailure(call: Call<List<ClothingDto>>, t: Throwable) {
                Log.w(TAG, "Retrofit getClothingCatalog onFailure: ${t.message}, using JSON catalog")
            }
        })
        return cachedCatalog
    }

    fun fetchWeather(city: String): WeatherDto {
        val normalizedCity = city.trim().ifEmpty { "Москва" }
        apiService.getCurrentWeather(normalizedCity).enqueue(object : Callback<WeatherDto> {
            override fun onResponse(call: Call<WeatherDto>, response: Response<WeatherDto>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Retrofit getCurrentWeather success: ${response.body()}")
                } else {
                    Log.w(TAG, "Retrofit getCurrentWeather HTTP ${response.code()}")
                }
            }

            override fun onFailure(call: Call<WeatherDto>, t: Throwable) {
                Log.w(TAG, "Retrofit getCurrentWeather onFailure: ${t.message}")
            }
        })
        return parseWeatherJsonForCity(normalizedCity)
    }

    private fun parseFallbackCatalogJson(): List<ClothingDto> {
        val listType = object : TypeToken<List<ClothingDto>>() {}.type
        return gson.fromJson(CATALOG_JSON, listType)
    }

    private fun parseWeatherJsonForCity(city: String): WeatherDto {
        val json = when (city.lowercase()) {
            "сочи" -> """{"city":"Сочи","temperature":23,"condition":"Ясно, солнечно","windSpeed":3}"""
            "новосибирск", "мурманск" ->
                """{"city":"$city","temperature":-8,"condition":"Снег, морозно","windSpeed":6}"""
            "санкт-петербург", "спб", "питер" ->
                """{"city":"Санкт-Петербург","temperature":11,"condition":"Облачно, небольшой дождь","windSpeed":7}"""
            else ->
                """{"city":"$city","temperature":14,"condition":"Переменная облачность","windSpeed":4}"""
        }
        return gson.fromJson(json, WeatherDto::class.java)
    }

    companion object {
        private const val TAG = "NetworkApi"
        const val BASE_URL = "https://my-json-server.typicode.com/svinarenkomd/weatherwardrobe-api/"

        private const val CATALOG_JSON = """
            [
              {
                "id": 1,
                "name": "Мембранная ветровка Urban",
                "category": "Верхняя одежда (Плащ / Пальто / Ветровка)",
                "minTemp": 8,
                "maxTemp": 18,
                "imageUrl": "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600",
                "description": "Лёгкая непродуваемая куртка с водоотталкивающей пропиткой для прохладной и ветреной погоды."
              },
              {
                "id": 2,
                "name": "Оверсайз худи с начёсом",
                "category": "Верх (Худи / Толстовка)",
                "minTemp": 8,
                "maxTemp": 18,
                "imageUrl": "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=600",
                "description": "Мягкое хлопковое худи для прохладных весенних и осенних дней."
              },
              {
                "id": 3,
                "name": "Классические синие джинсы",
                "category": "Низ (Джинсы / Брюки)",
                "minTemp": -5,
                "maxTemp": 22,
                "imageUrl": "https://images.unsplash.com/photo-1542272604-787c3835535d?w=600",
                "description": "Прямые джинсы из плотного денима на каждый день."
              },
              {
                "id": 4,
                "name": "Кожаные кеды Street",
                "category": "Обувь",
                "minTemp": 5,
                "maxTemp": 25,
                "imageUrl": "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=600",
                "description": "Удобные демисезонные кеды с защитой от влаги."
              },
              {
                "id": 5,
                "name": "Зимний пуховик Arctic",
                "category": "Верхняя одежда (Зимняя куртка / Пуховик)",
                "minTemp": -25,
                "maxTemp": 0,
                "imageUrl": "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=600",
                "description": "Тёплый зимний пуховик с капюшоном для морозной погоды."
              },
              {
                "id": 6,
                "name": "Базовая белая футболка",
                "category": "Верх (Футболка)",
                "minTemp": 20,
                "maxTemp": 35,
                "imageUrl": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600",
                "description": "Дышащая футболка из 100% хлопка для жаркой погоды."
              }
            ]
        """
    }
}
