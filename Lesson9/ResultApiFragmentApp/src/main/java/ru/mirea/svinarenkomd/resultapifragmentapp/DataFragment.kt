package ru.mirea.svinarenkomd.resultapifragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class DataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        val button = view.findViewById<Button>(R.id.buttonOpenBottomSheet)
        val editText = view.findViewById<EditText>(R.id.editTextInfo)

        button.setOnClickListener {
            val text = editText.text.toString()
            val bundle = Bundle()
            bundle.putString("key", text)

            childFragmentManager.setFragmentResult("requestKey", bundle)
            parentFragmentManager.setFragmentResult("requestKey", bundle)

            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(childFragmentManager, "ModalBottomSheet")
        }
        return view
    }
}
