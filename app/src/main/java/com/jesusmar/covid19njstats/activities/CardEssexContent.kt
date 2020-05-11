package com.jesusmar.covid19njstats.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jesusmar.covid19njstats.R
import kotlinx.android.synthetic.main.fragment_card_essex_content.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [CardEssexContent.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardEssexContent : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card_essex_content, container, false)
        view.tv_positive_essex_value.text = ""
        view.tv_negative_essex_value.text = ""
        view.tv_deaths_essex_value.text = ""
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = CardEssexContent()
            .apply {}
    }
}
