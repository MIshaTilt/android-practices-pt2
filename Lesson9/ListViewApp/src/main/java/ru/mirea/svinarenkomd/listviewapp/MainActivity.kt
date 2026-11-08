package ru.mirea.svinarenkomd.listviewapp

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

data class BookItem(
    val author: String,
    val title: String
) {
    override fun toString(): String = "$author — «$title»"
}

class MainActivity : AppCompatActivity() {

    private val books = arrayOf(
        BookItem("Роберт Мартин", "Чистая архитектура"),
        BookItem("Роберт Мартин", "Чистый код"),
        BookItem("Мартин Клеппман", "Высоконагруженные приложения"),
        BookItem("Эрик Эванс", "Предметно-ориентированное проектирование (DDD)"),
        BookItem("Джошуа Блох", "Java. Эффективное программирование"),
        BookItem("Дмитрий Жемеров, Светлана Исакова", "Kotlin в действии"),
        BookItem("Эрих Гамма, Ричард Хелм, Ральф Джонсон, Джон Влиссидес", "Приемы объектно-ориентированного проектирования (GoF)"),
        BookItem("Мартин Фаулер", "Рефакторинг. Улучшение проекта существующего кода"),
        BookItem("Мартин Фаулер", "Шаблоны корпоративных приложений"),
        BookItem("Стив Макконнелл", "Совершенный код"),
        BookItem("Томас Кормен, Чарльз Лейзерсон", "Алгоритмы: построение и анализ"),
        BookItem("Дональд Кнут", "Искусство программирования. Том 1"),
        BookItem("Дональд Кнут", "Искусство программирования. Том 2"),
        BookItem("Дональд Кнут", "Искусство программирования. Том 3"),
        BookItem("Эндрю Таненбаум", "Современные операционные системы"),
        BookItem("Эндрю Таненбаум", "Компьютерные сети"),
        BookItem("Брайан Гетц", "Java Concurrency на практике"),
        BookItem("Брюс Шнайер", "Прикладная криптография"),
        BookItem("Кевин Митник", "Искусство невидимости"),
        BookItem("Орелиен Жерон", "Прикладное машинное обучение с помощью Scikit-Learn, Keras и TensorFlow"),
        BookItem("Ян Гудфеллоу, Иошуа Бенджио", "Глубокое обучение"),
        BookItem("Франсуа Шолле", "Глубокое обучение на Python"),
        BookItem("Питер Норвиг, Стюарт Рассел", "Искусственный интеллект: современный подход"),
        BookItem("Федор Достоевский", "Братья Карамазовы"),
        BookItem("Федор Достоевский", "Идиот"),
        BookItem("Лев Толстой", "Война и мир"),
        BookItem("Лев Толстой", "Анна Каренина"),
        BookItem("Михаил Булгаков", "Мастер и Маргарита"),
        BookItem("Джордж Оруэлл", "1984"),
        BookItem("Олдос Хаксли", "О дивный новый мир"),
        BookItem("Фрэнк Герберт", "Дюна"),
        BookItem("Айзек Азимов", "Академия (Основание)"),
        BookItem("Станислав Лем", "Солярис"),
        BookItem("Аркадий и Борис Стругацкие", "Пикник на обочине"),
        BookItem("Рэй Брэдбери", "451 градус по Фаренгейту")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bookListView = findViewById<ListView>(R.id.book_list_view)

        val adapter = object : ArrayAdapter<BookItem>(
            this,
            android.R.layout.simple_list_item_2,
            android.R.id.text1,
            books
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val text1 = view.findViewById<TextView>(android.R.id.text1)
                val text2 = view.findViewById<TextView>(android.R.id.text2)
                val item = getItem(position)
                text1.text = "${position + 1}. ${item?.author.orEmpty()}"
                text2.text = "Книга: «${item?.title.orEmpty()}»"
                return view
            }
        }

        bookListView.adapter = adapter
    }
}
