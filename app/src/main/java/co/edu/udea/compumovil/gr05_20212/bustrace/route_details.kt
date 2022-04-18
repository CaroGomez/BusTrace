package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class route_details : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var go_back: Button
    private lateinit var go_maps: Button
    private lateinit var name_route: TextView
    private lateinit var user: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)
        MyToolbar().show(this, "Detalles de la ruta", true)
        user= FirebaseAuth.getInstance()

        database = Firebase.database.reference
        go_back = findViewById(R.id.go_back)
        go_maps = findViewById(R.id.view_map_route)
        //name_route = findViewById(R.id.route)
        go_back.setOnClickListener(){
            val intent = Intent (this, user_menu::class.java)
            startActivity(intent)
        }
        go_maps.setOnClickListener(){
            val intent = Intent (this, MapActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onCreateOptionsMenu (menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout) {
            user.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}