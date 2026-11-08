package ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.FragmentCatalogBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var clothingAdapter: ClothingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        clothingAdapter = ClothingAdapter { clickedItem ->
            mainViewModel.selectClothingItem(clickedItem)
            findNavController().navigate(R.id.action_catalog_to_details)
        }

        binding.recyclerViewCatalog.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCatalog.adapter = clothingAdapter

        mainViewModel.weatherState.observe(viewLifecycleOwner) { weather ->
            val tempSign = if (weather.temperature > 0) "+${weather.temperature}" else "${weather.temperature}"
            binding.textViewAppBarCity.text = weather.city
            binding.textViewWeatherTitle.text =
                "${weather.city} · ${tempSign}°C · ${weather.condition} (Ветер ${weather.windSpeed} м/с)"
            binding.textViewWeatherDesc.text = "Рекомендация: ${weather.recommendation}"
        }

        mainViewModel.combinedWardrobeState.observe(viewLifecycleOwner) { items ->
            clothingAdapter.setItems(items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
