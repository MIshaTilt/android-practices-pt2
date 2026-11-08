package ru.mirea.svinarenkomd.weatherwardrobe.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.FragmentProfileBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth.AuthActivity
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(requireContext())
        ).get(MainViewModel::class.java)

        mainViewModel.userProfileState.observe(viewLifecycleOwner) { profile ->
            if (profile.isAuthorized) {
                binding.textViewProfileEmail.text = profile.email
                binding.textViewProfileStatus.text = "Статус: Личный гардероб синхронизирован"
                binding.buttonLogout.text = "Выйти из аккаунта"
            } else {
                binding.textViewProfileEmail.text = "Гостевой режим"
                binding.textViewProfileStatus.text = "Статус: Доступен просмотр каталога и погоды"
                binding.buttonLogout.text = "Войти в аккаунт"
            }
            if (binding.editTextProfileCity.text.isNullOrBlank()) {
                binding.editTextProfileCity.setText(profile.preferredCity)
            }
        }

        binding.buttonSaveProfileSettings.setOnClickListener {
            val city = binding.editTextProfileCity.text.toString().trim().ifEmpty { "Москва" }
            mainViewModel.loadWeatherAndCatalog(city)
            Toast.makeText(
                requireContext(),
                "Город прогноза погоды обновлён: $city",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
