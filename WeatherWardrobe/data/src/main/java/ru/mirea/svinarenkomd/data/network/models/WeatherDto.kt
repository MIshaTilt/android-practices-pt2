package ru.mirea.svinarenkomd.data.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("city") @Expose val city: String,
    @SerializedName("temperature") @Expose val temperature: Int,
    @SerializedName("condition") @Expose val condition: String,
    @SerializedName("windSpeed") @Expose val windSpeed: Int
)
