package ru.mirea.svinarenkomd.weatherwardrobe.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth.AuthActivity
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        val textEmail = view.findViewById<TextView>(R.id.textViewProfileEmail)
        val textStatus = view.findViewById<TextView>(R.id.textViewProfileStatus)
        val editCity = view.findViewById<EditText>(R.id.editTextProfileCity)
        val buttonSaveSettings = view.findViewById<Button>(R.id.buttonSaveProfileSettings)
        val buttonLogout = view.findViewById<Button>(R.id.buttonLogout)

        mainViewModel.userProfileState.observe(viewLifecycleOwner) { profile ->
            if (profile.isAuthorized) {
                textEmail.text = profile.email
                textStatus.text = "Статус: Личный гардероб синхронизирован"
                buttonLogout.text = "Выйти из аккаунта"
            } else {
                textEmail.text = "Гостевой режим"
                textStatus.text = "Статус: Доступен просмотр каталога и погоды"
                buttonLogout.text = "Войти в аккаунт"
            }
            if (editCity.text.isNullOrBlank()) {
                editCity.setText(profile.preferredCity)
            }
        }

        buttonSaveSettings.setOnClickListener {
            val city = editCity.text.toString().trim().ifEmpty { "Москва" }
            mainViewModel.loadWeatherAndCatalog(city)
            Toast.makeText(
                requireContext(),
                "Город прогноза погоды обновлён: $city",
                Toast.LENGTH_SHORT
            ).show()
        }

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }
}
