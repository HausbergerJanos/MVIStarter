package com.hausberger.mvistarter.framework.presentation

import android.os.Bundle
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.framework.presentation.sample.SampleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SampleFragment())
            .commit()
    }
}