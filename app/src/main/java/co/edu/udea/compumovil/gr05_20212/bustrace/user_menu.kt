package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class user_menu : AppCompatActivity() {
    private lateinit var route1:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_menu)

        var consultas = Consultas()
        consultas.getRoutes()

        /*route1 = findViewById(R.id.btn_rute1)
        route1.setOnClickListener(){
            val intent = Intent (this, route_details::class.java)
            startActivity(intent)
        }*/
        //Referenciando el layout donde se van a crear los botones
        val layout = findViewById(R.id.layout) as LinearLayout

        //Pendiente traer la lista de rutas de la base de datos
        //val rutas = db.getRutas() por ejemplo
        // En vez de  for(i in 1..4) cambiarlo por la lista de rutas algo como for( ruta in rutas) donde rutas es la lista de rutas de la Base de datos
        for(i in 1..4) {

            // Creando el boton
            val button = Button(this)
            //creando layout parametros para las propiedades visuales del boton
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,20,0,0)
            button.layoutParams=params
            button.width=600
            button.height=300
            button.setBackgroundColor(resources.getColor(R.color.purple_500))
            button.setTextColor(Color.WHITE)
            //Aqui debe ir el nombre de la ruta que se obtenga de la BD EJ: ruta.nombre
            button.text = "Ruta "+ i
            //se crea un metodo click para el boton que va a transferir hacia la actividad de mapa , se envia como parametro el id de la ruta (ruta.id)
            button.setOnClickListener { Toast.makeText(this, "Este es el boton " + i , Toast.LENGTH_LONG).show() }

            //se agrega el boton a el layout
            layout.addView(button)
        }

    }
}