package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class user_menu : AppCompatActivity() {
    private lateinit var route1:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)

        route1 = findViewById(R.id.btn_rute1)
        route1.setOnClickListener(){
            val intent = Intent (this, route_details::class.java)
            startActivity(intent)
        }
    }
}