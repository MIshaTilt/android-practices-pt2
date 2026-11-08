package ru.mirea.svinarenkomd.fragmentapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class BlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(BlankFragment::class.java.simpleName, "onCreateView")
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberStudent = requireArguments().getInt("my_number_student")
        Log.d(BlankFragment::class.java.simpleName, numberStudent.toString())

        val textView = view.findViewById<TextView>(R.id.textViewStudentNumber)
        textView.text = "Номер по списку: $numberStudent\n(Свинаренко М.Д., БСБО-09-23)"
    }
}
