package co.edu.udea.compumovil.gr05_20212.bustrace

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
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
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.io.IOException
import java.io.InputStream

import java.net.URL


class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var  map:GoogleMap
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        MyToolbar().show(this, "Mapa de la ruta", true)
        user= FirebaseAuth.getInstance()
        CreateFragment()
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
    private fun  CreateFragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        createMarker()
    }

    private  fun createMarker() {
        val LatLongB = LatLngBounds.Builder()
        val origin = LatLng(6.1549805,-75.6040541)
        val destination = LatLng(6.1588622,-75.5997927)
        val waypoints= arrayListOf(LatLng(6.1589637,-75.5963223))
        map!!.addMarker(MarkerOptions().position(origin).title("Start Point"))
        map!!.addMarker(MarkerOptions().position(destination).title("End Point"))
        val url = getURL(origin,destination,waypoints)
        val options = createPolyLine()
        GlobalScope.launch(Dispatchers.IO) {
            var  result = URL(url).readText()
            //var result = read_json()
            withContext(Dispatchers.Main){

                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")

                val encodedPoints = routes!!["overview_polyline"]["points"][0].toString()
                val polypts = PolyUtil.decode(encodedPoints)
                // Add  points to polyline and bounds
                options.add(origin)
                LatLongB.include(origin)
                for (point in polypts)  {
                    options.add(point)
                    LatLongB.include(point)
                }
                /*  options.add(destination)
                  LatLongB.include(destination)*/
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                map!!.addPolyline(options)
                // show map with route centered
                map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }

        // map!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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