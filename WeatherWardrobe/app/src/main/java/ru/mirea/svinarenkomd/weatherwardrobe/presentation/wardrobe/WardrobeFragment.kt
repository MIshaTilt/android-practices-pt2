package ru.mirea.svinarenkomd.weatherwardrobe.presentation.wardrobe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog.ClothingAdapter
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.details.ClothingDetailsFragment
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.scanner.AiScannerFragment

class WardrobeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var clothingAdapter: ClothingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wardrobe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        val textWardrobeCount = view.findViewById<TextView>(R.id.textViewWardrobeCount)
        val layoutEmpty = view.findViewById<LinearLayout>(R.id.layoutEmptyWardrobe)
        val textEmptyTitle = view.findViewById<TextView>(R.id.textViewEmptyTitle)
        val textEmptyDesc = view.findViewById<TextView>(R.id.textViewEmptyDesc)
        val recyclerViewWardrobe = view.findViewById<RecyclerView>(R.id.recyclerViewWardrobe)
        val buttonOpenScanner = view.findViewById<Button>(R.id.buttonOpenScanner)

        clothingAdapter = ClothingAdapter { clickedItem ->
            mainViewModel.selectClothingItem(clickedItem)
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, ClothingDetailsFragment())
                .addToBackStack("details")
                .commit()
        }

        recyclerViewWardrobe.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewWardrobe.adapter = clothingAdapter

        val updateUi = {
            val profile = mainViewModel.userProfileState.value
            val roomItems = mainViewModel.roomWardrobeState.value.orEmpty()

            if (profile?.isAuthorized == false) {
                textWardrobeCount.text = "Гость"
                layoutEmpty.visibility = View.VISIBLE
                textEmptyTitle.text = "Режим Гостя"
                textEmptyDesc.text =
                    "Войдите в аккаунт во вкладке «Профиль», чтобы сохранять личный гардероб в БД Room и пользоваться ИИ-сканером."
                clothingAdapter.setItems(emptyList())
            } else {
                textWardrobeCount.text = "${roomItems.size} шт."
                if (roomItems.isEmpty()) {
                    layoutEmpty.visibility = View.VISIBLE
                    textEmptyTitle.text = "Гардероб пока пуст"
                    textEmptyDesc.text =
                        "Добавьте вещи из каталога или отсканируйте одежду камерой"
                } else {
                    layoutEmpty.visibility = View.GONE
                }
                clothingAdapter.setItems(roomItems)
            }
        }

        mainViewModel.userProfileState.observe(viewLifecycleOwner) { updateUi() }
        mainViewModel.roomWardrobeState.observe(viewLifecycleOwner) { updateUi() }

        buttonOpenScanner.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, AiScannerFragment())
                .addToBackStack("scanner")
                .commit()
        }
    }
}
