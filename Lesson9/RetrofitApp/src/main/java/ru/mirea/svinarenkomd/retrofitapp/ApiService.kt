package ru.mirea.svinarenkomd.retrofitapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("todos")
    fun getTodos(): Call<List<Todo>>

    @PUT("todos/{id}")
    fun updateTodo(
        @Path("id") id: Int,
        @Body todo: Todo
    ): Call<Todo>
}
