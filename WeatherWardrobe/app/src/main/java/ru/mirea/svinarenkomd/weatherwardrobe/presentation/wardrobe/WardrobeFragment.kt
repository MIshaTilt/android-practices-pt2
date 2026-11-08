package ru.mirea.svinarenkomd.weatherwardrobe.presentation.wardrobe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.FragmentWardrobeBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.catalog.ClothingAdapter
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class WardrobeFragment : Fragment() {

    private var _binding: FragmentWardrobeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var clothingAdapter: ClothingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWardrobeBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_wardrobe_to_details)
        }

        binding.recyclerViewWardrobe.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWardrobe.adapter = clothingAdapter

        val updateUi = {
            val profile = mainViewModel.userProfileState.value
            val roomItems = mainViewModel.roomWardrobeState.value.orEmpty()

            if (profile?.isAuthorized == false) {
                binding.textViewWardrobeCount.text = "Гость"
                binding.layoutEmptyWardrobe.visibility = View.VISIBLE
                binding.textViewEmptyTitle.text = "Режим Гостя"
                binding.textViewEmptyDesc.text =
                    "Войдите в аккаунт во вкладке «Профиль», чтобы сохранять личный гардероб в БД Room и пользоваться ИИ-сканером."
                clothingAdapter.setItems(emptyList())
            } else {
                binding.textViewWardrobeCount.text = "${roomItems.size} шт."
                if (roomItems.isEmpty()) {
                    binding.layoutEmptyWardrobe.visibility = View.VISIBLE
                    binding.textViewEmptyTitle.text = "Гардероб пока пуст"
                    binding.textViewEmptyDesc.text =
                        "Добавьте вещи из каталога или отсканируйте одежду камерой"
                } else {
                    binding.layoutEmptyWardrobe.visibility = View.GONE
                }
                clothingAdapter.setItems(roomItems)
            }
        }

        mainViewModel.userProfileState.observe(viewLifecycleOwner) { updateUi() }
        mainViewModel.roomWardrobeState.observe(viewLifecycleOwner) { updateUi() }

        binding.buttonOpenScanner.setOnClickListener {
            findNavController().navigate(R.id.action_wardrobe_to_scanner)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
