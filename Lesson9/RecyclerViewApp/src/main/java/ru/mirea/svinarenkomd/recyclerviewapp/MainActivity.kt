package ru.mirea.svinarenkomd.recyclerviewapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val events = getListData()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = EventRecyclerViewAdapter(events)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun getListData(): List<HistoricalEvent> {
        return listOf(
            HistoricalEvent(
                eventName = "1440 г. — Изобретение книгопечатания",
                description = "Иоганн Гутенберг создал первый печатный станок с подвижными литерами в Европе.",
                imageResName = "ic_event_history"
            ),
            HistoricalEvent(
                eventName = "1869 г. — Периодический закон Д.И. Менделеева",
                description = "Открытие периодической системы химических элементов.",
                imageResName = "ic_event_science"
            ),
            HistoricalEvent(
                eventName = "1895 г. — Изобретение радио А.С. Поповым",
                description = "Демонстрация первого в мире радиоприёмника на заседании РФХО.",
                imageResName = "ic_event_science"
            ),
            HistoricalEvent(
                eventName = "1946 г. — Запуск компьютера ENIAC",
                description = "Первый электронный цифровой вычислитель общего назначения.",
                imageResName = "ic_event_computer"
            ),
            HistoricalEvent(
                eventName = "1957 г. — Запуск «Спутника-1»",
                description = "СССР вывел на орбиту первый искусственный спутник Земли, открыв космическую эру.",
                imageResName = "ic_event_space"
            ),
            HistoricalEvent(
                eventName = "1961 г. — Первый полёт человека в космос",
                description = "12 апреля Юрий Гагарин совершил орбитальный облёт Земли на корабле «Восток-1».",
                imageResName = "ic_event_space"
            ),
            HistoricalEvent(
                eventName = "1969 г. — Создание сети ARPANET",
                description = "Первый сеанс связи между узлами сети — прообраза современного Интернета.",
                imageResName = "ic_event_computer"
            ),
            HistoricalEvent(
                eventName = "1991 г. — Появление Всемирной паутины (WWW)",
                description = "Тим Бернерс-Ли опубликовал первый веб-сайт и спецификацию HTTP/HTML.",
                imageResName = "ic_event_computer"
            ),
            HistoricalEvent(
                eventName = "2008 г. — Выход Android 1.0",
                description = "Релиз первой версии мобильной ОС Android и смартфона HTC Dream (T-Mobile G1).",
                imageResName = "ic_event_computer"
            ),
            HistoricalEvent(
                eventName = "2017 г. — Kotlin стал официальным языком Android",
                description = "Google объявила о первоклассной поддержке языка Kotlin для разработки под Android.",
                imageResName = "ic_event_computer"
            )
        )
    }
}
