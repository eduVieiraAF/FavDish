package com.example.favdish.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding

class AddUpdateActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding
            .inflate(layoutInflater)

        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Toast.makeText(
                this,
                "Point and click",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Toast.makeText(
                this,
                "Select image",
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}