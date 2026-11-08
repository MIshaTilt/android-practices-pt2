package ru.mirea.svinarenkomd.weatherwardrobe.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class ClothingDetailsFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clothing_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        val imagePhoto = view.findViewById<ImageView>(R.id.imageViewDetailsPhoto)
        val textTitle = view.findViewById<TextView>(R.id.textViewDetailsTitle)
        val textCategory = view.findViewById<TextView>(R.id.textViewDetailsCategory)
        val textDescription = view.findViewById<TextView>(R.id.textViewDetailsDescription)
        val buttonToggleWardrobe = view.findViewById<Button>(R.id.buttonToggleWardrobe)
        val buttonBack = view.findViewById<Button>(R.id.buttonBackToCatalog)

        mainViewModel.selectedClothingState.observe(viewLifecycleOwner) { item ->
            if (item == null) return@observe

            val minSign = if (item.minTemp > 0) "+${item.minTemp}" else "${item.minTemp}"
            val maxSign = if (item.maxTemp > 0) "+${item.maxTemp}" else "${item.maxTemp}"

            textTitle.text = "${item.name} (${minSign}°C..${maxSign}°C)"
            textCategory.text = "Категория: ${item.category}"
            textDescription.text = item.description

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
                    .into(imagePhoto)
            } else {
                imagePhoto.setImageResource(iconRes)
            }

            if (item.isFavorite) {
                buttonToggleWardrobe.text = "Удалить из моего гардероба"
                buttonToggleWardrobe.setBackgroundResource(R.drawable.bg_btn_secondary)
            } else {
                buttonToggleWardrobe.text = "Добавить в мой гардероб"
                buttonToggleWardrobe.setBackgroundResource(R.drawable.bg_btn_primary)
            }

            buttonToggleWardrobe.setOnClickListener {
                mainViewModel.toggleWardrobeItem(item)
            }
        }

        buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
