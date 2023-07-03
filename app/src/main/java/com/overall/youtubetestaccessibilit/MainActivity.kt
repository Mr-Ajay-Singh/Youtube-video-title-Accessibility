package com.overall.youtubetestaccessibilit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted->
            if (isGranted){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    var text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.startAccessibility)
        button.setOnClickListener {
            onSettingClick()
        }
         text = findViewById<TextView>(R.id.text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        val prefs = getSharedPreferences(
//            "testing", MODE_PRIVATE
//        )
//        text?.text = prefs.getString("testing","Hello World")
    }

    private fun onSettingClick() {
        try{
            startForResult.launch(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }catch (e: Exception){

        }
    }

}