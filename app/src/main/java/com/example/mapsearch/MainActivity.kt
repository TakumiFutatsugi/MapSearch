package com.example.mapsearch

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.*
import android.location.LocationListener
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(){
    /** 位置情報サービスクライアント */
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var editText: EditText

    /**
     * main
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.mainToolbar))

        /** permission処理 */
        startupPermission()

        /** MAP表示ボタン */
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
                /** インテントを受け取るアプリがあるかの確認 */
                val activities: List<ResolveInfo> = packageManager.queryIntentActivities(mapIntent, 0)
                val isIntentSafe: Boolean = activities.isNotEmpty()
                if (isIntentSafe) {
                    /** GoogleMap起動 */
                    startActivity(mapIntent)
                }
            }
        }
        editText = findViewById(R.id.locationKeyword)
    }

    /**
     * permission起動
     */
    private fun startupPermission() {
        Log.v("MainActivity", "startupPermission")
        /** 位置情報サービスクライアント作成 */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        /** 自動生成コード */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            /** 許可されていない場合、パーミッション要求ダイアログを表示 */
            Log.v("MainActivity", "startupPermission_true")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
            //return
        }
    }

    /** 認証結果 */
    override fun onRequestPermissionsResult( requestCode: Int,
                                             permissions: Array<out String>,
                                             grantResults: IntArray
    ) {
        Log.v("MainActivity", "onRequestPermissionsResult")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            recreate()
            requestLocationUpdates()
        }
    }

    /**
     * メニューアイテム選択
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.v("MainActivity", "onOptionsItemSelected")
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
        Log.v("MainActivity", "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /** 位置情報を取得 */
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    editText.setText(location.toString())
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1000.0f, locationListener)
        }
    }
}