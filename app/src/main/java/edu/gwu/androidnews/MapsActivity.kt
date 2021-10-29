package edu.gwu.androidnews

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.gwu.androidnews.databinding.ActivityMapsBinding
import org.jetbrains.anko.doAsync

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, ArticleClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMapsBinding
    private var currentAddress: Address? = null
    private var location: String = ""

    override fun onArticleClickListener(data: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
        startActivity(browserIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setTitle(R.string.map_title)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        googleMap.setOnMapLongClickListener { coords: LatLng ->
            googleMap.clear()

            // Geocoding should be done on a background thread - it involves networking
            // and has the potential to cause the app to freeze (Application Not Responding error)
            // if done on the UI Thread and it takes too long.
            doAsync {
                val geocoder = Geocoder(this@MapsActivity)

                // In Kotlin, you can assign the result of a try-catch block. Both the "try" and
                // "catch" clauses need to yield a valid value to assign.
                val results: List<Address> = try {
                    geocoder.getFromLocation(coords.latitude, coords.longitude, 10)
                } catch (exception: Exception) {
                    // Uses the error logger to print the error
                    Log.e("MapsActivity", "Geocoding failed", exception)

                    // Uses System.out.println to print the error
                    exception.printStackTrace()

                    listOf()
                }
                // Move back to the UI Thread now that we have some results to show.
                // The UI can only be updated from the UI Thread.
                runOnUiThread {
                    if (results.isNotEmpty()) {
                        // Potentially, we could show all results to the user to choose from,
                        // but for our usage it's sufficient enough to just use the first result.
                        // The Geocoder's first result is often the "best" one in terms of its accuracy / confidence.
                        val firstResult: Address = results[0]
                        val postalAddress: String = firstResult.getAddressLine(0)

                        Log.d("MapsActivity", "First result: $postalAddress")

                        // Add a map marker where the user tapped and pan the camera over

                        googleMap.addMarker(
                            MarkerOptions().position(coords).title(postalAddress)
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 10.0f))

                        currentAddress = firstResult

                        if (firstResult.getCountryCode() == "US")
                            location = firstResult.getAdminArea()
                        else
                            location = firstResult.getCountryName()

                        setTitle(getString(R.string.map_query_title, location))
                    } else {
                        Log.d("MapsActivity", "No results from geocoder!")

                        val toast = Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.geocoder_no_results),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }

                    doAsync {
                        val newsManager = NewsManager()

                        // make call to news API
                        val apiKey = getString(R.string.news_api_key)
                        Log.d("MapsActivity", "Retrieving articles from $location")
                        val articles: List<Article> = newsManager.retrieveArticlesInTitle(apiKey, location)

                        runOnUiThread {

                            recyclerView = findViewById(R.id.maps_recycler_view)

                            // Sets scrolling direction to vertical
                            recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity)

                            val adapter = ArticlesAdapter(this@MapsActivity, articles, this@MapsActivity)
                            recyclerView.adapter = adapter
                        }
                    }
                }
            }
        }
    }
}