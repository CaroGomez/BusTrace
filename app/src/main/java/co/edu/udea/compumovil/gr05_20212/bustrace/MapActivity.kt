package co.edu.udea.compumovil.gr05_20212.bustrace

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.CameraUpdateFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers.Main
import com.beust.klaxon.*
import com.google.android.gms.maps.model.LatLngBounds
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.io.IOException
import java.io.InputStream

import java.net.URL


class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var  map:GoogleMap
    var idRoute: Int =0
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        database = FirebaseDatabase.getInstance().getReference("Routes")
        idRoute= Integer.parseInt(intent.getStringExtra("id"))
        CreateFragment()
    }

    private fun  CreateFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        getRoutebyId(idRoute)
        //createMarker()
    }

    private  fun createMarker(routes: Route) {
        //val waypoints= arrayListOf(LatLng(6.1589637,-75.5963223))
        //val url = getURL(origin,destination,waypoints)
        val options = createPolyLine()


       // GlobalScope.launch() {
            //var  result = URL(url).readText()
           //var result = read_json()

            //withContext(Dispatchers.Main){
                val LatLongB = LatLngBounds.Builder()
                val origin = LatLng(routes.originLat.toDouble(),routes.originLon.toDouble())
                val destination = LatLng(routes.destinationLat.toDouble(),routes.destinationLon.toDouble())

                map!!.addMarker(MarkerOptions().position(origin).title("Start Point"))
                map!!.addMarker(MarkerOptions().position(destination).title("End Point"))
               /* val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")
                val encodedPoints = routes!!["overview_polyline"]["points"][0].toString()*/

                val encodedPoints = routes.wayPoints// asegurarse que no tiene doble "\\"
                val polypts = PolyUtil.decode(encodedPoints)
                // Add  points to polyline and bounds
                options.add(origin)
                LatLongB.include(origin)
                for (point in polypts)  {
                    options.add(point)
                    LatLongB.include(point)
                }
                  options.add(destination)
                  LatLongB.include(destination)
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                map!!.addPolyline(options)
                // show map with route centered
                map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
           // }
        //}

    }


    fun getRoutebyId(id: Int) {
        var route: Route = Route();
        database = FirebaseDatabase.getInstance().getReference("Routes")
        database.child(id.toString()).get().addOnSuccessListener {
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
                createMarker(route)
            }else {

            }
        }.addOnFailureListener(){

        }


    }

    private fun createPolyLine(): PolylineOptions {
        val options = PolylineOptions()
        options.color(Color.BLUE)
        options.width(10f)
        return options
    }

    private fun read_json():String?
    {
        var json : String?=null
        try {
            val inputStream : InputStream = assets.open("Routes.json")
            json = inputStream.bufferedReader().use {it.readText()  }

        }
        catch (e:IOException)
        {

        }
        return json
    }
    private fun getURL(from : LatLng, to : LatLng,wayPoints:List<LatLng>) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val key = "key="+getString(R.string.google_maps_key)
        var waypoints = "waypoints="
        wayPoints.map { waypoints= waypoints + it.latitude +","+it.longitude + "|"}
        waypoints = waypoints.dropLast(1)
        val params = "$origin&$dest&$sensor&$waypoints&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }


}