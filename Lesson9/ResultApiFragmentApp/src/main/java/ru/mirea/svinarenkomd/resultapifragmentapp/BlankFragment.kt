package ru.mirea.svinarenkomd.resultapifragmentapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class BlankFragment : Fragment() {

    private var listener: FragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        val image = view.findViewById<ImageView>(R.id.imageView)
        image.setOnClickListener {
            listener?.sendResult("image pushed")
        }
        return view
    }
}
