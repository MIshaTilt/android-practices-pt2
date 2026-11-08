package ru.mirea.svinarenkomd.fragmentmanagerapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class DetailsFragment : Fragment() {

    private lateinit var viewModel: ShareViewModel

    private val countryDetails = mapOf(
        "Россия" to "Столица: Москва\nКонтинент: Евразия\nОписание: Крупнейшее государство мира по площади, расположено в Восточной Европе и Северной Азии.",
        "Китай" to "Столица: Пекин\nКонтинент: Азия\nОписание: Государство в Восточной Азии с древнейшей историей и развитой высокотехнологичной экономикой.",
        "Индия" to "Столица: Нью-Дели\nКонтинент: Азия\nОписание: Страна в Южной Азии с богатым культурным наследием и быстрорастущим ИТ-сектором.",
        "Бразилия" to "Столица: Бразилиа\nКонтинент: Южная Америка\nОписание: Крупнейшая страна Южной Америки, известная бассейном Амазонки.",
        "Япония" to "Столица: Токио\nКонтинент: Азия\nОписание: Островное государство в Восточной Азии, технологический лидер.",
        "Германия" to "Столица: Берлин\nКонтинент: Европа\nОписание: Крупнейшая экономика Европейского союза в Центральной Европе.",
        "Франция" to "Столица: Париж\nКонтинент: Европа\nОписание: Государство в Западной Европе, мировой центр культуры, искусства и науки.",
        "Италия" to "Столица: Рим\nКонтинент: Европа\nОписание: Средиземноморское государство на Апеннинском полуострове, колыбель Ренессанса.",
        "Канада" to "Столица: Оттава\nКонтинент: Северная Америка\nОписание: Второе по площади государство мира, омываемое тремя океанами.",
        "Египет" to "Столица: Каир\nКонтинент: Африка / Азия\nОписание: Государство в Северной Африке и на Синайском полуострове, центр древней цивилизации."
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textTitle = view.findViewById<TextView>(R.id.textViewDetailsTitle)
        val textContent = view.findViewById<TextView>(R.id.textViewDetailsContent)

        viewModel = ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)
        viewModel.getSelectedItem().observe(viewLifecycleOwner) { country ->
            Log.d(DetailsFragment::class.java.simpleName, country)
            textTitle.text = "Страна: $country"
            textContent.text = countryDetails[country] ?: "Информация о стране $country"
        }
    }
}
