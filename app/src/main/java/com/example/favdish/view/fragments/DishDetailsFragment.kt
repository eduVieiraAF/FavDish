package com.example.favdish.view.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.favdish.R
import com.example.favdish.application.FavDishApplication
import com.example.favdish.databinding.FragmentDishDetailsBinding
import com.example.favdish.model.entities.FavDish
import com.example.favdish.utils.Constants
import com.example.favdish.viewmodel.FavDishViewModel
import com.example.favdish.viewmodel.FavDishViewModelFactory
import java.io.IOException

class DishDetailsFragment : Fragment() {

    private var mBinding: FragmentDishDetailsBinding? = null
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }
    private var mFavDishDetails: FavDish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        @Suppress("DEPRECATION")
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share_dish -> {
                val type = "text/plain"
                val subject = "Check out this recipe"
                var extraText = ""
                val shareWith = "Share with"

                mFavDishDetails?.let {
                    val image = ""

                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) {
                        it.image
                    }

                    extraText =
                        "$image " +
                                "\n\nTitle: ${it.title}\n\n" +
                                "Type: ${it.type}\n\n" +
                                "Category: ${it.category}" +
                                "\n\nTime required to cook dish is approx ${it.cookingTime} min" +
                                "\n\nIngredients: ${it.ingredients}" +
                                "\n\nInstructions: ${it.directionToCook}"
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)

                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }

        @Suppress("DEPRECATION")
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DishDetailsFragmentArgs by navArgs()

        mFavDishDetails = args.dishDetails

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
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
                                    val textColor1 = palette?.vibrantSwatch?.rgb ?: 0
                                    val textColor2 = palette?.darkVibrantSwatch?.rgb ?: 0
                                    val textColor3 = R.color.blue_grey_900
                                    if (textColor1 != 0){
                                        mBinding!!.tvTitle.setTextColor(textColor1)
                                        mBinding!!.tvCookingTime.setTextColor(textColor1)
                                        mBinding!!.tvCategory.setTextColor(textColor1)
                                    } else {
                                        if (textColor2 != 0) {
                                            mBinding!!.tvCategory.setTextColor(textColor2)
                                            mBinding!!.tvCookingTime.setTextColor(textColor2)
                                            mBinding!!.tvCategory.setTextColor(textColor2)
                                        } else {
                                            mBinding!!.tvTitle.setTextColor(textColor3)
                                            mBinding!!.tvCookingTime.setTextColor(textColor3)
                                            mBinding!!.tvCategory.setTextColor(textColor3)
                                        }
                                    }
                                }
                            }

                            return false
                        }
                    })
                    .into(mBinding!!.ivDishImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mBinding!!.tvTitle.text = it.dishDetails.title
            mBinding!!.tvType.text = it.dishDetails.type
            mBinding!!.tvCategory.text = it.dishDetails.category
            mBinding!!.tvIngredients.text = it.dishDetails.ingredients
            mBinding!!.tvCookingDirection.text = it.dishDetails.directionToCook
            mBinding!!.tvCookingTime.text = resources.getString(
                R.string.lbl_estimate_cooking_time,
                it.dishDetails.cookingTime
            )

            if (args.dishDetails.favoriteDish) {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
            } else {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
            }
        }

        mBinding!!.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

            mFavDishViewModel.update(args.dishDetails)

            if (args.dishDetails.favoriteDish) {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites, args.dishDetails.title),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_removed_fro_favorites, args.dishDetails.title),
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