package com.td.innovate.savingstracker.wearable;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by cassiadeering on 14-12-10.
 */
public class DataLayerListenerService extends WearableListenerService {

    private static GoogleApiClient mGoogleApiClient;

//    @Override
//    public int onStartCommand(Intent tent, int flags, int startId) {
//
//        //public void onCreate() {
//        //     super.onCreate();
//
//        //  Needed for communication between watch and device.
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                    @Override
//                    public void onConnected(Bundle connectionHint) {
//                        Log.d("DataL", "onConnected: " + connectionHint);
//                        tellPhoneConnectedState("connected");
//                        //  "onConnected: null" is normal.
//                        //  There's nothing in our bundle.
//                    }
//                    @Override
//                    public void onConnectionSuspended(int cause) {
//                        Log.d("DataL", "onConnectionSuspended: " + cause);
//                    }
//                })
//                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(ConnectionResult result) {
//                        Log.d("DataL", "onConnectionFailed: " + result);
//                    }
//                })
//                .addApi(Wearable.API)
//                .build();
//
//        mGoogleApiClient.connect();
//        Log.e("DataLListenerService", "onCreate");
//        return START_STICKY;
//
//    }

    /**
     * Here, the device actually receives the message that the phone sent, as a path.
     * We simply check that path's last segment and act accordingly.
     *
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.d("DataL", "message received " + messageEvent.getPath());
//
//        CardFrameManagementActivity mainActivity = new CardFrameManagementActivity();
//        mainActivity.sendMessage("ba humbug");

        String path = messageEvent.getPath();
        if (path.endsWith("pyf")) {
            Log.d("DataL", "pyf");
        } else if (isNumber(path)) {
            Log.d("DataL", "path is a number - pyf");
            Log.d("DataL", path);

            Intent intent = new Intent("PYF_DATA_RECEIVED");

            String[] numbersArray = path.split(",");

            intent.putExtra("NUMBERS", numbersArray);
            getApplicationContext().sendBroadcast(intent);

            //TODO - updatetextviews

        } else if (path.contains("start")) {
            new CardFrameManagementActivity.SendMessageOnOtherThread("pyf", null).start();

        }

    }

    private boolean isNumber(String path) {
        if (path.contains("1") || (path.contains("2") || path.contains("3")
                || path.contains("4") || path.contains("5") || path.contains("6")
                || path.contains("7") || path.contains("8") || path.contains("9")
                || path.contains("0")
        )) {
            return true;
        } else {
            return false;
        }
    }
}