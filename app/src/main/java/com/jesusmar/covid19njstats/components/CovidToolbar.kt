package com.jesusmar.covid19njstats.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.widget.Toolbar
import com.jesusmar.covid19njstats.R
import java.util.jar.Attributes

class CovidToolbar(context: Context) : Toolbar(context) {

    constructor(context: Context, pTitle: String) : this(context) {
        this.title = pTitle
        this.background = context.getDrawable(R.color.colorPrimary)
        this.popupTheme = R.style.customToolBarStyle
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        id = View.generateViewId()

    }
}