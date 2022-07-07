package com.example.favdish.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favdish.databinding.ActivityAddUpdateBinding

class AddUpdateActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAddUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener { onBackPressed() }
    }
}