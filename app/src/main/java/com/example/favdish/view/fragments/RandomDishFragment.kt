package com.example.favdish.view.fragments

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog() {
        mProgressDialog?.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRandomDishViewModel = ViewModelProvider(this)[RandomDishViewModel::class.java]
        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomDishViewModelObserver()

        mBinding!!.srlRandomDish.setColorSchemeResources(R.color.primary_color)
        mBinding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromAPI()
        }
    }

    private fun randomDishViewModelObserver() {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner) { randomDishResponse ->
            randomDishResponse?.let {
                if (mBinding!!.srlRandomDish.isRefreshing) {
                    mBinding!!.srlRandomDish.isRefreshing = false
                }

                setRandomDishResponseInUI(randomDishResponse.recipes[0])
            }
        }

        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner
        ) { dataError ->
            dataError?.let {
                if (mBinding!!.srlRandomDish.isRefreshing) {
                    mBinding!!.srlRandomDish.isRefreshing = false
                }

                Log.e("Random dish API error", "$dataError")
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(
            viewLifecycleOwner
        ) { loadRandomDish ->
            loadRandomDish?.let {
                if (loadRandomDish && !mBinding!!.srlRandomDish.isRefreshing) {
                    showCustomProgressDialog()
                } else hideCustomProgressDialog()

                Log.i("Random dish loading", "$loadRandomDish")
            }
        }
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {

        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop().listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("PaletteTag", "Failed loading bg color from image.", e)

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    resource.let {
                        Palette.from(resource!!.toBitmap()).generate { palette ->
                            val intColor = palette?.vibrantSwatch?.rgb ?: 0
                            mBinding!!.tvTitle.setTextColor(intColor)
                            mBinding!!.tvCookingTime.setTextColor(intColor)
                            mBinding!!.tvCategory.setTextColor(intColor)
                        }
                    }
                    return false
                }
            })
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
            ingredients = if (ingredients.isEmpty()) {
                value.original
            } else {
                ingredients + ", \n" + value.original
            }
        }

        mBinding!!.tvIngredients.text = ingredients
        mBinding!!.tvCookingDirection.text = recipe.instructions.capitalized()
        mBinding!!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        var addedToFavorites = false

        mBinding!!.tvCookingTime.text = resources.getString(
            R.string.lbl_estimate_cooking_time,
            recipe.readyInMinutes.toString()
        )

        mBinding!!.ivFavoriteDish.setOnClickListener {
            if (addedToFavorites) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites, recipe.title),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
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

                addedToFavorites = true

                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites, recipe.title),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mBinding = null
    }
}