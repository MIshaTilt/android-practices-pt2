package ru.mirea.svinarenkomd.weatherwardrobe.presentation.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.FragmentAiScannerBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class AiScannerFragment : Fragment() {

    private var _binding: FragmentAiScannerBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        mainViewModel.recognitionState.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                val minSign = if (result.suggestedMinTemp > 0) "+${result.suggestedMinTemp}" else "${result.suggestedMinTemp}"
                val maxSign = if (result.suggestedMaxTemp > 0) "+${result.suggestedMaxTemp}" else "${result.suggestedMaxTemp}"
                binding.textViewRecognizedTitle.text =
                    "Распознано: ${result.recognizedLabel} (${(result.confidence * 100).toInt()}%)"
                binding.textViewRecognizedDesc.text =
                    "Категория: ${result.suggestedCategory} · Комфортная температура от ${minSign}°C до ${maxSign}°C"
            }
        }

        binding.buttonTakePhoto.setOnClickListener {
            mainViewModel.recognizeSamplePhoto()
        }

        binding.buttonSaveScannedItem.setOnClickListener {
            mainViewModel.saveRecognizedItemToWardrobe()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
