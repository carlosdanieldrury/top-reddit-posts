package com.drury.topredditposts.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drury.topredditposts.R
import org.koin.core.component.KoinComponent


class MainActivity : AppCompatActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
