package ru.mirea.svinarenkomd.resultapifragmentapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var receivedText: String = ""
    private var textViewResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val text = bundle.getString("key").orEmpty()
            receivedText = text
            Log.d(BottomSheetFragment::class.java.simpleName, "Get text $text")
            textViewResult?.text = "Получен текст через Fragment Result API:\n«$text»"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        textViewResult = view.findViewById(R.id.textViewBottomSheetResult)
        if (receivedText.isNotEmpty()) {
            textViewResult?.text = "Получен текст через Fragment Result API:\n«$receivedText»"
        }
        return view
    }
}
