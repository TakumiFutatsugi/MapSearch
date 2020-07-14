package com.example.mapsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GpsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.gpsToolbar))

        /** 戻るボタン追加 */
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        } ?: IllegalAccessException("Toolbar cannot be null")
    }

    override fun onBackPressed() {
        /** Do Something */
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}