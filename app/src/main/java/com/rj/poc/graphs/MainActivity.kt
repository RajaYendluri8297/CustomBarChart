package com.rj.poc.graphs

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.transition.Fade

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tvBarChart).apply {
            text = "Hello, World!"
        }
          val bundle = bundleOf("raja" to "RAJ")

        val intent = Intent(this, BarChartActivity::class.java).apply {
            putExtras(bundle)
        }
        startActivity(intent)

        val tvBarChart = findViewById<TextView>(R.id.tvBarChart)
        tvBarChart.setOnClickListener {
            startActivity(Intent(this@MainActivity,BarChartActivity::class.java))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
                overrideActivityTransition(AppCompatActivity.OVERRIDE_TRANSITION_CLOSE, Fade.MODE_IN,Fade.MODE_OUT)
            }else{
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out_anim)
            }
        }

    }
}