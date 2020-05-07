package com.jesusmar.covid19njstats.activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jesusmar.covid19njstats.CardEssexContent
import com.jesusmar.covid19njstats.CardStateContent

class TabLayoutAdapter(fm: FragmentManager, behavior: Int, private val totalTabs: Int) : FragmentPagerAdapter(fm, behavior) {

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
        return totalTabs
    }

}


