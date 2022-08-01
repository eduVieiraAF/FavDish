package com.example.favdish.view.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentRandomDishBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.model.entities.RandomDish
import com.example.favdish.utils.Constants
import com.example.favdish.utils.capitalized
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
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
                setRandomDishResponseInUI(randomDishResponse.recipes[0])
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

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {

        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(mBinding!!.ivDishImage)

        mBinding!!.tvTitle.text = recipe.title.capitalized()

        var dishType = "Other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0].capitalized()
            mBinding!!.tvType.text = dishType.capitalized()
        }

        mBinding!!.tvCategory.text = dishType.capitalized()

        var ingredients = ""
        for (value in recipe.extendedIngredients) {
            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        mBinding!!.tvIngredients.text = ingredients

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            mBinding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        mBinding!!.tvCookingTime.text = resources.getString(
            R.string.lbl_estimate_cooking_time,
            recipe.readyInMinutes.toString()
        )

        mBinding!!.ivFavoriteDish.setOnClickListener {
            val randomDishDetails = FavDish(
                recipe.image,
                Constants.DISH_IMAGE_SOURCE_ONLINE,
                recipe.title.capitalized(),
                dishType.capitalized(),
                "Other",
                ingredients,
                recipe.readyInMinutes.toString(),
                recipe.instructions,
                true
            )

            val mFavDishViewModel: FavDishViewModel by viewModels {
                FavDishViewModelFactory(
                    (requireActivity()
                        .application as FavDishApplication).repository
                )
            }

            mFavDishViewModel.insert(randomDishDetails)

            mBinding!!.ivFavoriteDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_favorite_selected
                )
            )

            Toast.makeText(
                requireActivity(),
                resources.getString(R.string.msg_added_to_favorites),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}