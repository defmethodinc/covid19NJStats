package com.jesusmar.covid19njstats.activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabLayoutAdapter(fm: FragmentManager, private val behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CardStateContent()
            }
            1 -> {
                CardEssexContent()
            }
            else -> null
        }!!
    }

    override fun getCount(): Int {
        return behavior
    }

}


