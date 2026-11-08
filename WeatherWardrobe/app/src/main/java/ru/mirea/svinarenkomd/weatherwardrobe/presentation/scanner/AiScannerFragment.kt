package ru.mirea.svinarenkomd.weatherwardrobe.presentation.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class AiScannerFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ai_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        val textRecognizedTitle = view.findViewById<TextView>(R.id.textViewRecognizedTitle)
        val textRecognizedDesc = view.findViewById<TextView>(R.id.textViewRecognizedDesc)
        val buttonTakePhoto = view.findViewById<Button>(R.id.buttonTakePhoto)
        val buttonSaveScannedItem = view.findViewById<Button>(R.id.buttonSaveScannedItem)

        mainViewModel.recognitionState.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                val minSign = if (result.suggestedMinTemp > 0) "+${result.suggestedMinTemp}" else "${result.suggestedMinTemp}"
                val maxSign = if (result.suggestedMaxTemp > 0) "+${result.suggestedMaxTemp}" else "${result.suggestedMaxTemp}"
                textRecognizedTitle.text =
                    "Распознано: ${result.recognizedLabel} (${(result.confidence * 100).toInt()}%)"
                textRecognizedDesc.text =
                    "Категория: ${result.suggestedCategory} · Комфортная температура от ${minSign}°C до ${maxSign}°C"
            }
        }

        buttonTakePhoto.setOnClickListener {
            mainViewModel.recognizeSamplePhoto()
        }

        buttonSaveScannedItem.setOnClickListener {
            mainViewModel.saveRecognizedItemToWardrobe()
        }
    }
}
