package edu.gwu.androidnews

import android.content.Intent
import android.location.Address
import android.location.Geocoder
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
import com.google.android.material.button.MaterialButton
import edu.gwu.androidnews.databinding.ActivityMapsBinding
import org.jetbrains.anko.doAsync

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMapsBinding
    private var currentAddress: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val articles: List<Article> = getFakeArticles()
        //val articles: List<Article> = listOf<Article>()
        recyclerView = findViewById(R.id.recycler_view)

        // Sets scrolling direction to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter: ArticlesAdapter = ArticlesAdapter(articles)
        recyclerView.adapter = adapter
    }


    fun updateCurrentAddress(address: Address) {
        currentAddress = address
        setTitle(address.getAddressLine(0))
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
                val geocoder: Geocoder = Geocoder(this@MapsActivity)

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

                        updateCurrentAddress(firstResult)
                    } else {
                        Log.d("MapsActivity", "No results from geocoder!")

                        val toast = Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.geocoder_no_results),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
            }
        }
    }

    fun getFakeArticles(): List<Article> {
        return listOf(
            Article(
                title = "Nick Capurso",
                source = "@nickcapurso",
                content = "We're learning lists!",
                iconUrl = "https://...."
            ),
            Article(
                title = "Android Central",
                source = "@androidcentral",
                content = "NVIDIA Shield TV vs. Shield TV Pro: Which should I buy?",
                iconUrl = "https://...."
            ),
            Article(
                title = "DC Android",
                source = "@DCAndroid",
                content = "FYI - another great integration for the @Firebase platform",
                iconUrl = "https://...."
            ),
            Article(
                title = "KotlinConf",
                source = "@kotlinconf",
                content = "Can't make it to KotlinConf this year? We have a surprise for you. We'll be live streaming the keynotes, closing panel and an entire track over the 2 main conference days. Sign-up to get notified once we go live!",
                iconUrl = "https://...."
            ),
            Article(
                title = "Android Summit",
                source = "@androidsummit",
                content = "What a #Keynote! @SlatteryClaire is the Director of Performance at Speechless, and that's exactly how she left us after her amazing (and interactive!) #keynote at #androidsummit. #DCTech #AndroidDev #Android",
                iconUrl = "https://...."
            ),
            Article(
                title = "Fragmented Podcast",
                source = "@FragmentedCast",
                content = ".... annnnnnnnnd we're back!\n\nThis week @donnfelker talks about how it's Ok to not know everything and how to set yourself up mentally for JIT (Just In Time [learning]). Listen in here: \nhttp://fragmentedpodcast.com/episodes/135/ ",
                iconUrl = "https://...."
            ),
            Article(
                title = "Jake Wharton",
                source = "@JakeWharton",
                content = "Free idea: location-aware physical password list inside a password manager. Mostly for garage door codes and the like. I want to open my password app, switch to the non-URL password section, and see a list of things sorted by physical distance to me.",
                iconUrl = "https://...."
            ),
            Article(
                title = "Droidcon Boston",
                source = "@droidconbos",
                content = "#DroidconBos will be back in Boston next year on April 8-9!",
                iconUrl = "https://...."
            ),
            Article(
                title = "AndroidWeekly",
                source = "@androidweekly",
                content = "Latest Android Weekly Issue 327 is out!\nhttp://androidweekly.net/ #latest-issue  #AndroidDev",
                iconUrl = "https://...."
            ),
            Article(
                title = ".droidconSF",
                source = "@droidconSF",
                content = "Drum roll please.. Announcing droidcon SF 2018! November 19-20 @ Mission Bay Conference Center. Content and programming by @tsmith & @joenrv.",
                iconUrl = "https://...."
            )
        )
    }
}