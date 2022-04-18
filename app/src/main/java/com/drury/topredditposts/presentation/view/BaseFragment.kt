package com.drury.topredditposts.presentation.view

import androidx.fragment.app.Fragment
import org.koin.core.component.KoinComponent

abstract class BaseFragment : Fragment(), KoinComponent {

    protected fun setupBackButton(show: Boolean = false) {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(show)
            (activity as MainActivity?)?.supportActionBar?.setHomeButtonEnabled(show)
        }
    }
}