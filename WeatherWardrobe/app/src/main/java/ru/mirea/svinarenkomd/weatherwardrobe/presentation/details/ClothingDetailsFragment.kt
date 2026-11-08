package ru.mirea.svinarenkomd.weatherwardrobe.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.FragmentClothingDetailsBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class ClothingDetailsFragment : Fragment() {

    private var _binding: FragmentClothingDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClothingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        mainViewModel.selectedClothingState.observe(viewLifecycleOwner) { item ->
            if (item == null) return@observe

            val minSign = if (item.minTemp > 0) "+${item.minTemp}" else "${item.minTemp}"
            val maxSign = if (item.maxTemp > 0) "+${item.maxTemp}" else "${item.maxTemp}"

            binding.textViewDetailsTitle.text = "${item.name} (${minSign}°C..${maxSign}°C)"
            binding.textViewDetailsCategory.text = "Категория: ${item.category}"
            binding.textViewDetailsDescription.text = item.description

            val iconRes = when {
                item.category.contains("Верхняя", ignoreCase = true) -> R.drawable.ic_clothing_outerwear
                item.category.contains("Низ", ignoreCase = true) -> R.drawable.ic_clothing_bottom
                item.category.contains("Обувь", ignoreCase = true) -> R.drawable.ic_clothing_shoes
                else -> R.drawable.ic_clothing_top
            }

            if (item.imageUrl.isNotBlank()) {
                Picasso.get()
                    .load(item.imageUrl)
                    .placeholder(iconRes)
                    .error(iconRes)
                    .resize(600, 400)
                    .centerCrop()
                    .into(binding.imageViewDetailsPhoto)
            } else {
                binding.imageViewDetailsPhoto.setImageResource(iconRes)
            }

            if (item.isFavorite) {
                binding.buttonToggleWardrobe.text = "Удалить из моего гардероба"
                binding.buttonToggleWardrobe.setBackgroundResource(R.drawable.bg_btn_secondary)
            } else {
                binding.buttonToggleWardrobe.text = "Добавить в мой гардероб"
                binding.buttonToggleWardrobe.setBackgroundResource(R.drawable.bg_btn_primary)
            }

            binding.buttonToggleWardrobe.setOnClickListener {
                mainViewModel.toggleWardrobeItem(item)
            }
        }

        binding.buttonBackToCatalog.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
