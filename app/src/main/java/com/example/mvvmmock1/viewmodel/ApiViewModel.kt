package com.example.mvvmmock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmmock1.model.DataModel
import com.example.mvvmmock1.repository.ApiRepository

class ApiViewModel(): ViewModel() {
    private val data = MutableLiveData<DataModel>()
    private val apiRepository = ApiRepository(this)

    init {
        apiRepository.getApi()
    }

     fun getDataFromRepository(dataModel: DataModel) {
        this.data.value = dataModel
    }

     fun getData(): LiveData<DataModel> = data
}