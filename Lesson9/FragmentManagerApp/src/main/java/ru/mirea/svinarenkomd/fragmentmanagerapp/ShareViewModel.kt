package ru.mirea.svinarenkomd.fragmentmanagerapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {
    private val selectedItem = MutableLiveData<String>()

    fun selectItem(item: String) {
        selectedItem.value = item
    }

    fun getSelectedItem(): LiveData<String> = selectedItem
}
