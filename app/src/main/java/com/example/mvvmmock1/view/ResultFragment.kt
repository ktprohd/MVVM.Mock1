package com.example.mvvmmock1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mvvmmock1.R
import com.example.mvvmmock1.databinding.FragmentResultBinding
import com.example.mvvmmock1.model.Score
import com.example.mvvmmock1.viewmodel.QuizViewModel

class ResultFragment : Fragment() {

   private lateinit var binding: FragmentResultBinding
    private val userViewModel: QuizViewModel by viewModels()
    var myList = ArrayList<Score>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getScore().observe(viewLifecycleOwner, Observer { users ->
            myList = users

            if (myList.isNotEmpty()) {
                binding.txvcorrect1.text=myList[0].numOfCorrect.toString()
                binding.txvwrong1.text=(3-myList[0].numOfCorrect).toString()
            }
        })

        binding.btnexit.setOnClickListener {
            activity?.finish()
        }

        binding.btnplayagain.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_section, QuizFragment())
            transaction?.commit()
        }
    }
}