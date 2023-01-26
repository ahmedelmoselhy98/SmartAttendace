package com.elmoselhy.smartattendace.uilayer.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.elmoselhy.smartattendace.datalayer.firebase.MyFirebaseController
import com.elmoselhy.smartattendace.datalayer.session.Preference
import com.elmoselhy.smartattendace.uilayer.app.BaseApp
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {
    lateinit var myApp: BaseApp

    @Inject
    lateinit var preference: Preference
    @Inject
    lateinit var firebaseController: MyFirebaseController

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