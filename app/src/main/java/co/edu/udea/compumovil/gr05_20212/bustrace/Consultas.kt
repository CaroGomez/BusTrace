package co.edu.udea.compumovil.gr05_20212.bustrace

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Consultas {
    private lateinit var database: DatabaseReference


    fun getRoutes(): List<Route> {

        var routes: List<Route> = listOf()
        database = FirebaseDatabase.getInstance().getReference("Routes")
        database.get().addOnSuccessListener {
            if(it.exists()){
                for (route in it.children) {
                    var routeToAdd = Route(route.child("id").value.toString(), route.child("nombre").value.toString())
                    routes.toMutableList().add(routeToAdd)
                }
            }
        }

        return routes
    }

    fun getRoutebyId(id: String): Route {
        var route: Route = Route("",  "");
        database = FirebaseDatabase.getInstance().getReference("Routes")
        database.child(id).get().addOnSuccessListener {
            if(it.exists()){
                val name = it.child("nombre").value.toString()
                val destinationLat = it.child("destino_lat").value.toString()
                val destinationLon = it.child("destino_lon").value.toString()
                val originLat = it.child("origin_lat").value.toString()
                val originLon = it.child("origin_lon").value.toString()
                val description = it.child("descripcion").value.toString()
                val wayPoints = it.child("way_points").value.toString()

                route = Route(name, destinationLat, destinationLon, originLat, originLon, description,wayPoints)
            }else {

            }
        }.addOnFailureListener(){

        }
        return route
    }

}