package com.example.mapsearch

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(){
    /** 位置情報サービスクライアント */
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    /** 入力欄 */
    private lateinit var editText: EditText
    /** 検索履歴アダプター */
    private lateinit var historyAdapter : ArrayAdapter<String>
    /** 検索履歴リストビュー */
    private lateinit var historyListView: ListView

    /**
     * main
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** 入力欄 */
        editText = findViewById(R.id.locationKeyword)
        /** アダプター。配列やListを1つずつ表示するのに使用する */
        historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        /** 検索履歴 */
        historyListView = findViewById(R.id.historyListView)
        /** MAP表示ボタン */
        val showMapButton = findViewById<Button>(R.id.mainShowMapButton)
        /** ToolbarをActionbarとして使用する */
        setSupportActionBar(findViewById(R.id.mainToolbar))

        /** permission処理 */
        startupPermission()

        /** MAP表示ボタン押下 */
        showMapButton.setOnClickListener{
            Log.d("MainActivity", "showMapButton.setOnClickListener")
            /** 入力したキーワードを取得 */
            val locationKeyword = editText.text.toString()
            if( locationKeyword == "" ){
                /** TODO 未入力なら警告を出す */
            }
            else {
                addHistoryList(locationKeyword)
                startMapApp(locationKeyword)
            }
        }
        /** リスト項目タップイベント */
        historyListView.setOnItemClickListener{parent, view, position, id ->
            val keyword = view.findViewById<TextView>(android.R.id.text1).text.toString()
            startMapApp(keyword)
        }

        /** リスト項目長押しイベント */
        historyListView.setOnItemLongClickListener { parent, view, position, id ->
            Log.d("MainActivity", "historyListView.setOnItemLongClickListener")
            AlertDialog.Builder(this).apply {
                setTitle("削除しますか？")
                setPositiveButton("OK") { _, _ ->
                    historyAdapter.remove(historyAdapter.getItem(position))
                }
                setNegativeButton("NG") { _, _ ->
                    /** Do Nothing */
                }
                show()
            }
            return@setOnItemLongClickListener true
        }
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

    /**
     * permissionリクエスト結果
     */
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
            /** TODO 確認ダイアログ
             * 履歴を全削除
             */
            }
            /** 現在地ボタン押下 */
            R.id.location -> {
                /** 検索履歴に追加 */
                addHistoryList("現在地")
                /** ListVuewにタッチイベントを追加するにはOnItemClickListnerを使用する */
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
     * 検索履歴に追加
     */
    private fun addHistoryList(keyword: String) {
        /** TODO 文字列チェック */
        /** TODO 履歴の更新
         * 現在時刻の取得と項目への複数要素格納
         * 履歴の配列を取得
         * ダブった項目は配列の最後尾へ移動
         */
        if(keyword != "") {
            /** 検索文字列をアダプターの先頭に追加 */
            historyAdapter.insert(keyword,0)
            /** ListViewに、生成したアダプターを設定 */
            historyListView.adapter = historyAdapter
        }
    }


    /**
     * Toolbarのメニュー表示
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.v("MainActivity", "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 位置情報を取得
     */
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1.0f, locationListener)
        }
    }

    /**
     * 地図アプリ起動
     */
    private fun startMapApp(keyword: String){
        Log.d("MainActivity", "startMapApp")
        /** 入力文字列をエンコード */
        val uri = Uri.encode(keyword)
        val location = Uri.parse("geo:0,0?q=$uri")
        val mapIntent = Intent(Intent.ACTION_VIEW, location)

        /** インテントを受け取るアプリがあるかの確認 */
        val activities= packageManager.queryIntentActivities(mapIntent, 0)
        val isIntentSafe = activities.isNotEmpty()
        if (isIntentSafe) {
            /** GoogleMap起動 */
            startActivity(mapIntent)
        }
    }
}