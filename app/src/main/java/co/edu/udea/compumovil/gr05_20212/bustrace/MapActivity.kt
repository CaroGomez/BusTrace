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
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.io.IOException
import java.io.InputStream

import java.net.URL


class MapActivity : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var  map:GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        CreateFragment()
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
        val origin = LatLng(6.1582966,-75.6060953)
        val destination = LatLng(6.1551013,-75.6093979)
        map!!.addMarker(MarkerOptions().position(origin).title("Marker in Sydney"))
        map!!.addMarker(MarkerOptions().position(destination).title("Marker in Opera"))
        val url = getURL(origin,destination)
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
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }
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
    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val key = "key="+getString(R.string.google_maps_key)
        val params = "$origin&$dest&$sensor&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

}