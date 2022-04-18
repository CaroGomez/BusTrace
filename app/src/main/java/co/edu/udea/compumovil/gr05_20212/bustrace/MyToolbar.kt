package co.edu.udea.compumovil.gr05_20212.bustrace

import android.view.Menu
import androidx.appcompat.app.AppCompatActivity


class MyToolbar {
    fun show(activities : AppCompatActivity, title:String, upButton:Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.myToolbar))
        activities.supportActionBar?.title=title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

}