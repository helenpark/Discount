package com.td.innovate.savingstracker.wearable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class CardFrameManagementActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {
    private static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_frame_management);
        ((GridViewPager) findViewById(R.id.pager)).setAdapter(new SampleGridPagerAdapter(this, getFragmentManager()));
        Intent listenerServiceIntent = new Intent(CardFrameManagementActivity.this, DataLayerListenerService.class);
        CardFrameManagementActivity.this.startService(listenerServiceIntent);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("OOPS", "Main Activity onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        new SendMessageOnOtherThread("/start", null).start();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("OOPS", "Main Activity onConnectionSuspended: " + i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("OOPS", "Main Activity onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    static class SendMessageOnOtherThread extends Thread {
        String path;
        String message;

        // Constructor to send a message to the data layer
        SendMessageOnOtherThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, null).await();
                if (result.getStatus().isSuccess()) {
                    Log.v("OOPS", "Message: {" + message + "} sent to: " + node.getDisplayName());
                } else {
                    // Log an error
                    Log.v("OOPS", "ERROR: failed to send wearable message");
                }
            }
        }
    }
}






