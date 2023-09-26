package com.example.mvvmmock1.repository

import com.example.mvvmmock1.api.ApiService
import com.example.mvvmmock1.model.DataModel
import com.example.mvvmmock1.viewmodel.ApiViewModel
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit

class ApiRepository(private val apiViewModel: ApiViewModel) {
    fun getApi() {
        getjokes() {
            apiViewModel.getDataFromRepository(it)  // nhu 1 ham callback de xu ly bat dong bo
        }
    }
    fun getjokes( callback: (DataModel) -> Unit) {

        // Create a Retrofit instance with the base URL and
        // a GsonConverterFactory for parsing the response.
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/").addConverterFactory(
                GsonConverterFactory.create()).build()

        // Create an ApiService instance from the Retrofit instance.
        val service: ApiService = retrofit.create<ApiService>(ApiService::class.java)

        // Call the getjokes() method of the ApiService
        // to make an API request.
        val call: Call<DataModel> = service.getjokes()

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(object : Callback<DataModel> {
            // This is an anonymous inner class that implements the Callback interface.
            override fun onResponse(response: Response<DataModel>?, retrofit: Retrofit?) {
                // This method is called when the API response is received successfully.

                if(response!!.isSuccess){
                    // If the response is successful, parse the
                    // response body to a DataModel object.
                    val jokes: DataModel = response.body() as DataModel

                    // Call the callback function with the DataModel
                    // object as a parameter.
                    callback(jokes)
                }
            }

            override fun onFailure(t: Throwable?) {
            }
        })
    }
}