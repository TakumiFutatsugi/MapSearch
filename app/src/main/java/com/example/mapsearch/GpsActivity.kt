package com.example.mapsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GpsActivity : AppCompatActivity(){
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.gpsToolbar))

        /** Toolbarに戻るボタン追加 */
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
    }

    /** 戻るボタン押下 */
    override fun onBackPressed() {
        /** Do Something */
        super.onBackPressed()
    }

    /** ナビゲーションバーの戻るボタン押下 */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}