package com.example.lifecycle.ui.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.lifecycle.R
import com.example.lifecycle.base.BaseActivity
import com.example.lifecycle.ui.activity.findfileguide.FindFileGuideActivity
import com.photo.utils.Constants
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (isTaskRoot) init()
        else goToNextActivity()
    }

    private fun init(intent: Intent? = this.intent) {
        bt_import_data.setOnClickListener {
            startActivity(
                Intent(this,
                    FindFileGuideActivity::class.java)
            )
        }

        bt_ensure.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun goToNextActivity(intent: Intent? = this.intent) {
        // for the url_scheme , need invoke finish first
        finish()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(0, 0)
    }

}
