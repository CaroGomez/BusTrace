package co.edu.udea.compumovil.gr05_20212.bustrace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class driver_menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_menu)
        MyToolbar().show(this, "Menú conductor", false)
    }
}