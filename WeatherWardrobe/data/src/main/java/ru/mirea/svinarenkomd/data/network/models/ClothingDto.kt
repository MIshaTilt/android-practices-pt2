package ru.mirea.svinarenkomd.data.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ClothingDto(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("name") @Expose val name: String,
    @SerializedName("category") @Expose val category: String,
    @SerializedName("minTemp") @Expose val minTemp: Int,
    @SerializedName("maxTemp") @Expose val maxTemp: Int,
    @SerializedName("imageUrl") @Expose val imageUrl: String,
    @SerializedName("description") @Expose val description: String
)
