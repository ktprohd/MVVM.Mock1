package com.example.mvvmmock1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmmock1.model.Score
import com.example.mvvmmock1.model.User
import com.example.mvvmmock1.repository.DataRepository

class QuizViewModel(): ViewModel() {
    private val questionList = MutableLiveData<ArrayList<User>>()
    private val scoreList = MutableLiveData<ArrayList<Score>>()

    private val dataRepository = DataRepository(this)

    init {
        dataRepository.getQuestions()
        dataRepository.getScore()
    }

    fun fetchQuestions(question: ArrayList<User>) {
        questionList.value = question
    }

        fun fetchScore(score1: ArrayList<Score>) {
            scoreList.value = score1
        }

        fun getQuestionList(): LiveData<ArrayList<User>> = questionList
        fun getScore(): LiveData<ArrayList<Score>> = scoreList
    }
