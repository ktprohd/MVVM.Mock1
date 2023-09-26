package com.example.mvvmmock1.view

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mvvmmock1.R
import com.example.mvvmmock1.databinding.FragmentQuizBinding
import com.example.mvvmmock1.model.Score
import com.example.mvvmmock1.model.Question
import com.example.mvvmmock1.viewmodel.QuizViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    var countDownTimer: CountDownTimer? = null
    private val handler = Handler(Looper.getMainLooper())
    var i:Int = 1
    var correct:Int=0
    var wrong:Int=0
    var result:String? =""
    val email1 = Firebase.auth.currentUser
    private val userViewModel: QuizViewModel by viewModels()
    var myList = ArrayList<Question>()
    val database = Firebase.database
    val myRefscore = database.getReference("Score")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progessbar.visibility = View.VISIBLE
        binding.layoutquestion.visibility = View.GONE

        userViewModel.getQuestionList().observe(viewLifecycleOwner, Observer { users ->
            myList=users
            if (myList.isNotEmpty()) {
                readQuiz(0)
                result = myList[0].trueAnswer
                setProgessBar()}
        })
        timer()
        onClick()
    }

    fun readQuiz(i:Int){
        binding.edtquestion.text=myList[i].question
        binding.btna.text=myList[i].answer1
        binding.btnb.text=myList[i].answer2
        binding.btnc.text=myList[i].answer3
        binding.btnd.text=myList[i].answer4
        result=myList[i].trueAnswer
    }

    fun onClick(){

        binding.btnnext.setOnClickListener {
                resetButton()
                setButtonEnable()
                timer()
                binding.txvcorrect.text= correct.toString()
                binding.txvwrong.text= wrong.toString()
                if (i<3){
                    readQuiz(i)
                    i++}
                else{
                    i=1
                    alertDialog()
                }
        }

        binding.btna.setOnClickListener {
            if (result == "A") {
                binding.btna.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btna.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnb.setOnClickListener {
            if (result == "B") {
                binding.btnb.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnb.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnc.setOnClickListener {
            if (result == "C") {
                binding.btnc.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnc.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnd.setOnClickListener {
            if (result == "D") {
                binding.btnd.setBackgroundResource(R.drawable.btn_style2)
                correct++
            } else {
                binding.btnd.setBackgroundResource(R.drawable.btn_style3)
                wrong++
                showTrueAnswer()
            }
            setButtonDisable()
        }

        binding.btnfinish.setOnClickListener {
            i=3
            binding.btnnext.performClick()
        }
    }

    fun resetButton(){
        binding.btna.setBackgroundResource(R.drawable.btn_style)
        binding.btnb.setBackgroundResource(R.drawable.btn_style)
        binding.btnc.setBackgroundResource(R.drawable.btn_style)
        binding.btnd.setBackgroundResource(R.drawable.btn_style)
    }

    fun setProgessBar(){
        if (result != "") {
            binding.progessbar.visibility = View.GONE
            binding.layoutquestion.visibility = View.VISIBLE
        }
    }

    fun showTrueAnswer(){
        when(result){
            "A" -> binding.btna.setBackgroundResource(R.drawable.btn_style2)
            "B" -> binding.btnb.setBackgroundResource(R.drawable.btn_style2)
            "C" -> binding.btnc.setBackgroundResource(R.drawable.btn_style2)
            "D" -> binding.btnd.setBackgroundResource(R.drawable.btn_style2)
        }
    }

    fun setButtonEnable(){
        binding.btna.isEnabled=true
        binding.btnd.isEnabled=true
        binding.btnb.isEnabled=true
        binding.btnc.isEnabled=true
    }

    fun setButtonDisable(){
        binding.btna.isEnabled=false
        binding.btnd.isEnabled=false
        binding.btnb.isEnabled=false
        binding.btnc.isEnabled=false
    }

    fun checkAnswered(){
        if (wrong+correct<i) { wrong++}
        Log.d("LoginActivity", "Wrong: $wrong ")
    }

    fun timer() {
        countDownTimer?.cancel()
        countDownTimer = object: CountDownTimer(7000,1000) {
            override fun onTick(p0: Long) {
                binding.timer.text = "${p0/1000}"
                binding.txvcorrect.text= correct.toString()
            }
            override fun onFinish() {
                binding.timer.text = "0"
                binding.edtquestion.text = resources.getString(R.string.timeup)
                checkAnswered()
                handler.postDelayed({
                    // Thực hiện tác vụ bạn muốn sau khi đã trôi qua khoảng thời gian delayMillis
                    binding.btnnext.performClick()
                }, 4000)
            }
        }
        countDownTimer?.start()
    }

    fun alertDialog(){
        val dialog= AlertDialog.Builder(requireActivity())
        dialog.apply {
            setTitle("QuizGame")
            setMessage(getText(R.string.alertmessage))
            setNegativeButton("Play Again"){ dialogInterface: DialogInterface, a:Int->
                readQuiz(0)
                i=1

            }
            setPositiveButton("SeeResult"){ dialogInterface: DialogInterface, a:Int->
                val email = email1?.email
                val score = Score(email,correct)
                addScore(score)
                correct = 0
                wrong = 0
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragment_section, ResultFragment())?.addToBackStack(null)?.commit()
            }
        }

        dialog.show()
    }

    fun addScore(score: Score){
        myRefscore.child("1").setValue(score)
        // gọi ở AlertDialog để sau gửi lên mỗi lần hoàn thành Quiz
    }

}