package com.example.favdish.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favdish.R

class AddUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update)

        supportActionBar!!.hide()
    }
}