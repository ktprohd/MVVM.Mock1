package com.example.mvvmmock1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvmmock1.R
import com.example.mvvmmock1.databinding.FragmentResultBinding
import com.example.mvvmmock1.databinding.FragmentStartBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val email = it.email

        binding.txtwelcom.text= "Welcome $email"}

        binding.startquiz.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_section, QuizFragment())
            transaction?.commit()
        }

        binding.btnsignout.setOnClickListener {
            Firebase.auth.signOut()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_section, LoginFragment())
            transaction?.commit()
        }
    }
}