package ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.details.ClothingDetailsFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class CatalogFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var clothingAdapter: ClothingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        val textAppBarCity = view.findViewById<TextView>(R.id.textViewAppBarCity)
        val textWeatherTitle = view.findViewById<TextView>(R.id.textViewWeatherTitle)
        val textWeatherDesc = view.findViewById<TextView>(R.id.textViewWeatherDesc)
        val recyclerViewCatalog = view.findViewById<RecyclerView>(R.id.recyclerViewCatalog)

        clothingAdapter = ClothingAdapter { clickedItem ->
            mainViewModel.selectClothingItem(clickedItem)
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, ClothingDetailsFragment())
                .addToBackStack("details")
                .commit()
        }

        recyclerViewCatalog.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewCatalog.adapter = clothingAdapter

        mainViewModel.weatherState.observe(viewLifecycleOwner) { weather ->
            val tempSign = if (weather.temperature > 0) "+${weather.temperature}" else "${weather.temperature}"
            textAppBarCity.text = weather.city
            textWeatherTitle.text =
                "${weather.city} · ${tempSign}°C · ${weather.condition} (Ветер ${weather.windSpeed} м/с)"
            textWeatherDesc.text = "Рекомендация: ${weather.recommendation}"
        }

        mainViewModel.combinedWardrobeState.observe(viewLifecycleOwner) { items ->
            clothingAdapter.setItems(items)
        }
    }
}
