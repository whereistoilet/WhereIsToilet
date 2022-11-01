package com.ich.whereistoilet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object{
        const val KEY = "756a4a4c7169636836306146674772"
        const val BASE_URL = "http://openAPI.seoul.go.kr:8088"
        const val REQUEST_URL = "/json/SearchPublicToiletPOIService/1/5/"
    }
}