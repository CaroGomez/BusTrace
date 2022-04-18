package co.edu.udea.compumovil.gr05_20212.bustrace


import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Consultas {
    private lateinit var database: DatabaseReference


    suspend fun getRoutes(): MutableList<Route> {
        val routes: MutableList<Route> = mutableListOf()
        database = FirebaseDatabase.getInstance().getReference("Routes")
        val job = GlobalScope.launch {
            database.get().addOnSuccessListener {
                for (route in it.children) {
                    var routeToAdd = Route()
                    routeToAdd.id = route.child("id").value.toString()
                    routeToAdd.name = route.child("nombre").value.toString()
                    routes.add(routeToAdd)
                    Log.i("Test", routeToAdd.name);
                }
            }
        }
        job.join()
        delay(3000L)
        Log.i("Test", routes.toString());
        return routes
    }

    suspend fun getRoutebyId(id: String): Route {
        var route: Route = Route();
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

                route = Route()
                route.name = name
                route.destinationLat = destinationLat
                route.destinationLon = destinationLon
                route.originLat = originLat
                route.originLon = originLon
                route.description = description
                route.wayPoints = wayPoints
            }else {

            }
        }.addOnFailureListener(){

        }
        delay(1000L)
        return route
    }

}