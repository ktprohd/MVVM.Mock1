package com.example.mvvmmock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmmock1.model.Score
import com.example.mvvmmock1.model.Question
import com.example.mvvmmock1.repository.DataRepository

class QuizViewModel(): ViewModel() {
    private val questionList = MutableLiveData<ArrayList<Question>>()
    private val scoreList = MutableLiveData<ArrayList<Score>>()
    private val dataRepository = DataRepository(this)

    init {
        dataRepository.getQuestions()
        dataRepository.getScore()
    }

    fun fetchQuestions(question: ArrayList<Question>) {
        questionList.value = question
    }
    fun fetchScore(score1: ArrayList<Score>) {
            scoreList.value = score1
        }


    fun getQuestionList(): LiveData<ArrayList<Question>> = questionList
    fun getScore(): LiveData<ArrayList<Score>> = scoreList
}
