package com.example.mapsearch

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    /**
     * main
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.mainToolbar))

        val showMapButton = findViewById<Button>(R.id.mainShowMapButton)
        showMapButton.setOnClickListener{
            /** 入力したキーワードを取得 */
            val editText = findViewById<EditText>(R.id.locationKeyword)
            val locationKeyword = editText.text.toString()
            if( locationKeyword == "" ){
                /** 未入力なら警告を出す */
            }
            else {
                /** 入力文字列をエンコード */
                val uri = Uri.encode(locationKeyword)
                val location = Uri.parse("geo:0,0?q=$uri")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                /** 対応するアプリが存在するかの判定 */
                val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
                val isIntentSafe: Boolean = activities.isNotEmpty()
                if (isIntentSafe) {
                    /** GoogleMap起動 */
                    startActivity(mapIntent)
                }
            }
        }
    }

    /**
     * メニューアイテム選択
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            /** 削除ボタン押下 */
            R.id.delete -> {

            }
            /** MAP表示ボタン押下 */
            R.id.location -> {
                val intent = Intent(applicationContext, GpsActivity::class.java)
                startActivity(intent)
            }
            /** 終了ボタン押下 */
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