package in.bitcode.locationbasedservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class PABR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isEntering = intent.getBooleanExtra(
                LocationManager.KEY_PROXIMITY_ENTERING, false
        );

        Log.e("tag", "Entering " + isEntering + "");

    }
}
