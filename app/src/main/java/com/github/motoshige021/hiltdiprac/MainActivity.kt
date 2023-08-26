package com.github.motoshige021.hiltdiprac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.motoshige021.hiltdiprac.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            /*　Fragment transition changed to describe in navigation.xml
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
             */
        }
    }
}