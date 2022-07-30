package com.example.favdish.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {
    private var mBinding: FragmentRandomDishBinding? = null
    private lateinit var mRandomDishViewModel: RandomDishViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRandomDishViewModel = ViewModelProvider(this)[RandomDishViewModel::class.java]
        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner
        ) { randomDishResponse ->
            randomDishResponse?.let {
                Log.i("Random dish Response", "${randomDishResponse.recipes[0]}")
            }
        }

        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner
        ) { dataError ->
            dataError?.let {
                Log.e("Random dish API error", "$dataError")
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(
            viewLifecycleOwner
        ) { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random dish loading", "$loadRandomDish")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}