package com.example.mvvmmock1.repository


import com.example.mvvmmock1.model.Score
import com.example.mvvmmock1.model.User
import com.example.mvvmmock1.viewmodel.QuizViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataRepository(private val quizViewModel: QuizViewModel) {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceScore: DatabaseReference
    private lateinit var questionList: ArrayList<User>
    private lateinit var scoreList: ArrayList<Score>

    fun getQuestions() {
        databaseReference = FirebaseDatabase.getInstance().getReference("question")
        questionList = arrayListOf()

        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                if (snapshot.exists()) {
                    val questionList = arrayListOf<User>()
                    for (quesSnap in snapshot.children) {
                        val question = quesSnap.getValue(User::class.java)
                        questionList.add(question!!)
                    }
                    quizViewModel.fetchQuestions(questionList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getScore(){
        databaseReferenceScore =FirebaseDatabase.getInstance().getReference("Score")
        scoreList = arrayListOf()
        databaseReferenceScore.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scoreList.clear()
                if (snapshot.exists()) {
                    val scoreList = arrayListOf<Score>()
                    for (scoreSnap in snapshot.children) {
                        val score = scoreSnap.getValue(Score::class.java)
                        scoreList.add(score!!)
                    }
                    quizViewModel.fetchScore(scoreList)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}