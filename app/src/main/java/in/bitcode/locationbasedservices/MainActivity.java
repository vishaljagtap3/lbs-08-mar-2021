package in.bitcode.locationbasedservices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    BroadcastReceiver brLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location =
            intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
            mt("new location: " + location.getLatitude()  + " , " + location.getLongitude());
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providerNames = locationManager.getAllProviders();
        for (String proName : providerNames) {
            mt(proName);

            LocationProvider locationProvider = locationManager.getProvider(proName);
            mt("Power: " + locationProvider.getPowerRequirement());
            mt("REq sat: " + locationProvider.requiresSatellite());
            mt("Alt? " + locationProvider.supportsAltitude());
            mt("cost? " + locationProvider.hasMonetaryCost());
            mt("Acc: " + locationProvider.getAccuracy());
            mt("Cell? " + locationProvider.requiresCell());
            mt("Net? " + locationProvider.requiresNetwork());

            @SuppressLint("MissingPermission")
            Location location = locationManager.getLastKnownLocation(proName);
            if (location != null) {
                mt("location: " + location.getLatitude() + " , " + location.getLongitude() + " , " + location.getAltitude() + " --> " + location.getAccuracy());

            }
            mt("----------------------------------");

        }


        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String bestProvider = locationManager.getBestProvider(criteria, true);
        mt("Best Provider: " + bestProvider);

        //locationManager.get

        /*
        LocationListener listener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                bestProvider,
                1000,
                10,
                listener
        );
        */

        registerReceiver(
                brLocation,
                new IntentFilter("in.bitcode.KR")
        );

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                10,
                PendingIntent.getBroadcast(
                        this,
                        1,
                        new Intent("in.bitcode.KR"),
                        0
                )
        );


        registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Location location =
                                intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
                        mt("single new location: " + location.getLatitude()  + " , " + location.getLongitude());
                    }
                },
                new IntentFilter("in.bitcode.single.KR")
        );


        locationManager.requestSingleUpdate(
                LocationManager.NETWORK_PROVIDER,
                PendingIntent.getBroadcast(
                        this,
                        1,
                        new Intent("in.bitcode.single.KR"),
                        0
                )
        );

        /*locationManager.removeUpdates(
                PendingIntent.getBroadcast(
                        this,
                        1,
                        new Intent("in.bitcode.KR"),
                        0
                )
        );*/


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                1,
                new Intent("in.bitcode.KR"),
                0
        );

        //locationManager.removeUpdates(listener);
        locationManager.addProximityAlert(
                18.51234,
                72.51234,
                50000,
                -1, //System.ctinmilis() + 2hrs
                pendingIntent
        );

        //locationManager.removeProximityAlert(pendingIntent);

    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            mt(location.getLatitude() + " -- " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }

    private void mt(String text) {
        Log.e("tag", text);
    }
}