package com.jesusmar.covid19njstats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card_state_content.*
import kotlinx.android.synthetic.main.fragment_card_state_content.view.*

class CardStateContent : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_state_content, container, false)
        view.tv_positive_state_value.text = ""
        view.tv_negative_state_value.text = ""
        view.tv_deaths_state_value.text = ""
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = CardStateContent().apply {}
    }
}
