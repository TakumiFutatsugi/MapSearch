package com.example.mapsearch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /**
     * main
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.mainToolbar))
    }

    /**
     * メニューアイテム選択
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> {}
            R.id.location -> {
                val intent = Intent(applicationContext, GpsActivity::class.java)
                startActivity(intent)
            }
            R.id.finish -> {
                finish()
            }
        else -> {
            return super.onOptionsItemSelected(item)
        }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Toolbarのメニュー表示
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}