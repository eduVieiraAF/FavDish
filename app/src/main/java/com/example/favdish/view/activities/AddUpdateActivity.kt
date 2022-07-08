package com.example.favdish.view.activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.example.favdish.R
import com.example.favdish.databinding.ActivityAddUpdateBinding
import com.example.favdish.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

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
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        Toast.makeText(
                            this@AddUpdateActivity,
                            "Camera enabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermission()
                }
            }
            ).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
          Dexter.withContext(this@AddUpdateActivity)
              .withPermissions(
                  Manifest.permission.READ_EXTERNAL_STORAGE,
                  Manifest.permission.WRITE_EXTERNAL_STORAGE
              )
              .withListener(object : MultiplePermissionsListener {
                  override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                      if(report!!.areAllPermissionsGranted()) {
                          Toast.makeText(
                              this@AddUpdateActivity,
                              "Access to gallery granted",
                              Toast.LENGTH_SHORT
                          ).show()
                      }
                  }

                  override fun onPermissionRationaleShouldBeShown(
                      permissions: MutableList<PermissionRequest>?,
                      token: PermissionToken?
                  ) {
                      showRationalDialogForPermission()
                  }
              }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage(
                "It seems you have declined permissions required for this feature. " +
                        "Go to Application Settings to enable these permissions."
            )
            .setPositiveButton(R.string.ad_go_to_settings) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)

                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") {dialog,_ ->
                dialog.dismiss()
            }.show()
    }
}