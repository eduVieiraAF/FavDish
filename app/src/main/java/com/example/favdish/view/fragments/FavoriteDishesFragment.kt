package com.example.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentFavoriteDishesBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.view.activities.MainActivity
import com.example.favdish.view.adapters.FavDishAdapter
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var mBinding: FragmentFavoriteDishesBinding? = null

    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    //private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                mBinding!!.rvFavoriteDishesList.layoutManager = GridLayoutManager(
                    requireActivity(), 2
                )

                val adapter = FavDishAdapter(this)
                mBinding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    mBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                } else {
                    mBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    mBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(favDish: FavDish) {

        findNavController().navigate(
            FavoriteDishesFragmentDirections
                .actionFavoriteDishesToDishDetails(favDish)
        )

        if (requireActivity() is MainActivity) (activity as MainActivity?)!!
            .hideBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) (activity as MainActivity?)!!
            .showBottomNavigationView()
    }
}