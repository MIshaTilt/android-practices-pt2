package ru.mirea.svinarenkomd.retrofitapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoAdapter(
    private val context: Context,
    private val todos: List<Todo>,
    private val apiService: ApiService
) : RecyclerView.Adapter<TodoViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val imageUrls = listOf(
        "https://images.unsplash.com/photo-1484480974693-6ca0a78fb36b?w=300",
        "https://images.unsplash.com/photo-1517842645767-c639042777db?w=300",
        "https://images.unsplash.com/photo-1455390582262-044cdead277a?w=300",
        "https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?w=300",
        "https://images.unsplash.com/photo-1513542789411-b6a5d4f31634?w=300"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = layoutInflater.inflate(R.layout.item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.textViewTitle.text = todo.title

        // Снимаем старый слушатель перед установкой состояния CheckBox при переиспользовании ViewHolder
        holder.checkBoxCompleted.setOnCheckedChangeListener(null)
        holder.checkBoxCompleted.isChecked = todo.completed == true

        val imageUrl = imageUrls[position % imageUrls.size]
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.ic_todo_placeholder)
            .error(R.drawable.ic_todo_error)
            .resize(140, 140)
            .centerCrop()
            .into(holder.imageViewAvatar)

        holder.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
            todo.completed = isChecked
            val todoId = todo.id ?: return@setOnCheckedChangeListener

            apiService.updateTodo(todoId, todo).enqueue(object : Callback<Todo> {
                override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Todo #$todoId updated: completed=$isChecked (HTTP ${response.code()})")
                        Toast.makeText(
                            context,
                            "Todo #$todoId обновлён на сервере (completed=$isChecked)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e(TAG, "updateTodo error code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Todo>, t: Throwable) {
                    Log.e(TAG, "updateTodo onFailure: ${t.message}")
                    Toast.makeText(
                        context,
                        "Ошибка сети при обновлении Todo #$todoId",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun getItemCount(): Int = todos.size

    companion object {
        private const val TAG = "TodoAdapter"
    }
}
