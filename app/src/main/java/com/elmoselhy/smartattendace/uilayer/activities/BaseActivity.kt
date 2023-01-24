package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.elmoselhy.smartattendace.uilayer.app.BaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    lateinit var myApp: BaseApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApp = this.applicationContext as BaseApp
        val locale = Locale("ar")
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        //configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.displayMetrics)
        setContentView(setUpLayoutView())
        init()
    }

    protected abstract fun setUpLayoutView(): View
    protected abstract fun init()

}