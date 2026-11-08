package ru.mirea.svinarenkomd.fragmentmanagerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HeaderFragment : Fragment() {

    private lateinit var viewModel: ShareViewModel

    private val countries = listOf(
        "Россия",
        "Китай",
        "Индия",
        "Бразилия",
        "Япония",
        "Германия",
        "Франция",
        "Италия",
        "Канада",
        "Египет"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShareViewModel::class.java)

        val listView = view.findViewById<ListView>(R.id.listViewCountries)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            countries
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            viewModel.selectItem(countries[position])
        }

        if (viewModel.getSelectedItem().value == null) {
            viewModel.selectItem(countries.first())
        }
    }

    companion object {
        private const val ARG_TITLE = "title"

        fun newInstance(title: String): HeaderFragment {
            val fragment = HeaderFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
