package ru.mirea.svinarenkomd.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.mirea.svinarenkomd.data.network.models.ClothingDto
import ru.mirea.svinarenkomd.data.network.models.WeatherDto

interface RetrofitApiService {
    @GET("clothing")
    fun getClothingCatalog(): Call<List<ClothingDto>>

    @GET("weather")
    fun getCurrentWeather(@Query("city") city: String): Call<WeatherDto>
}
