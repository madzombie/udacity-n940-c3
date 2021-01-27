package com.udacity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        extras.let {
            val id = extras?.getLong("id",-1)
            val url = extras?.getString("url")
            val urlText = findViewById<TextView>(R.id.filename_textView)
            val successIDText = findViewById<TextView>(R.id.status_textView)
            urlText.text=url
            if (id?.toInt()!! <0) {
                successIDText.text=getString(R.string.error)
            } else {
                successIDText.text=getText(R.string.ok)
            }
        }
    }

}
