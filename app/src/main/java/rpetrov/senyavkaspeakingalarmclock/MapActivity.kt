package rpetrov.senyavkaspeakingalarmclock

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.view.View
import com.j256.ormlite.logger.LoggerFactory
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import rpetrov.senyavkaspeakingalarmclock.kotterknife.bindView


/**
 * Created by Roman Petrov
 */
class MapActivity : AppCompatActivity() {
    private val logger = LoggerFactory.getLogger(MapActivity::class.java)

    val map: MapView by bindView(R.id.map)
    val btCenterMap: AppCompatImageButton by bindView(R.id.ic_center_map)

    var itemizedIconOverlay : ItemizedIconOverlay<OverlayItem>? = null

    private var currentLocation: Location? = null

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            currentLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String?) {}

        override fun onProviderDisabled(provider: String?) {}
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.map_activity)

        map.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)

            controller.setZoom(9.5)
            controller.setCenter(GeoPoint(59.9343, 30.3351))

            val mapEventsOverlay = MapEventsOverlay(object: MapEventsReceiver {
                override fun longPressHelper(p: GeoPoint?): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun singleTapConfirmedHelper(location: GeoPoint?): Boolean {

                    if(location == null)
                        return false

                    val mItems = java.util.ArrayList<OverlayItem>()
                    val olItem = OverlayItem("", "", GeoPoint(location.latitude, location.longitude))
                    val newMarker: Drawable
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        newMarker = Application.instance().resources.getDrawable(R.drawable.map_marker_b, theme)
                    } else {
                        newMarker = Application.instance().resources.getDrawable(R.drawable.map_marker_b)
                    }
                    newMarker.setTint(ContextCompat.getColor(this@MapActivity, R.color.colorPrimary))
                    olItem.setMarker(newMarker)
                    mItems.add(olItem)

                    map.overlays.remove(itemizedIconOverlay)
                    itemizedIconOverlay = ItemizedIconOverlay(mItems, null, Application.instance())
                    map.overlays.add(itemizedIconOverlay)
                    map.invalidate()
                    return true
                }
            })
            overlays.add(0, mapEventsOverlay)
        }

        btCenterMap.setOnClickListener(View.OnClickListener {
            logger.info("centerMap clicked ")
            currentLocation?.apply {
                val myPosition = GeoPoint(latitude, longitude)
                map.controller.animateTo(myPosition)
            }
        })
    }

    private fun checkPermissions() {
        val requiredPermissions: ArrayList<String> = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!requiredPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    requiredPermissions.toTypedArray(),
                    0)
        }
    }

    public override fun onResume() {
        super.onResume()
        checkPermissions()
        map.onResume() //needed for compass, my location overlays, v6.0.0 and up

        val lm = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)
        }
    }

    public override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause()  //needed for compass, my location overlays, v6.0.0 and up

        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        lm.removeUpdates(locationListener)
    }
}
