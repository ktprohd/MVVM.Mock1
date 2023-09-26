package com.example.mvvmmock1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mvvmmock1.databinding.FragmentApiBinding
import com.example.mvvmmock1.viewmodel.ApiViewModel


class ApiFragment : Fragment() {

    private lateinit var binding: FragmentApiBinding
    private val apiViewModel: ApiViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnJoke.setOnClickListener {
            apiViewModel.getData().observe(viewLifecycleOwner, Observer { users ->
                binding.tvJoke.text = it.id.toString()
            })
        }
    }
}

