package ru.mirea.svinarenkomd.retrofitapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val call = apiService.getTodos()
        call.enqueue(object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                if (response.isSuccessful && response.body() != null) {
                    val todos = response.body()!!
                    todoAdapter = TodoAdapter(this@MainActivity, todos, apiService)
                    recyclerView.adapter = todoAdapter
                } else {
                    Log.e(TAG, "onResponse: ${response.code()}")
                    Toast.makeText(
                        this@MainActivity,
                        "Ошибка ответа сервера: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Ошибка загрузки списка дел: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    companion object {
        const val TAG = "MainActivity"
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
