package io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubViewModel : ViewModel() {
    private val mutableCounter: MutableLiveData<Int> = MutableLiveData(0)
    val counter: LiveData<Int> = mutableCounter

    fun increaseCounter() {
        mutableCounter.value?.let {
            mutableCounter.value = it + 1
        } ?: run {
            mutableCounter.value = 1
        }
    }
}