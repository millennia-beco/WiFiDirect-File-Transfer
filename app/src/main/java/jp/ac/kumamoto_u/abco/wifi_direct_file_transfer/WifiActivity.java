package jp.ac.kumamoto_u.abco.wifi_direct_file_transfer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by AbeKodai on 2016/11/14.
 */
public class WifiActivity extends Activity {
    WifiP2pManager mManager;
    //WifiP2pManager.Channel ：　アプリケーションをWi-Fi P2Pフレームワークに接続するために使用される
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;

    IntentFilter mIntentFilter;

    WifiActivity(){}


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null); //ここでinitialize！ Channelを返すらしい
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

    //Create an intent filter and add the same intents that your broadcast receiver checks for:
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


    }

    //Check to see if Wi-Fi P2P is on and supported. A good place to check this is in your broadcast receiver
    // when it receives the WIFI_P2P_STATE_CHANGED_ACTION intent. Notify your activity of the Wi-Fi P2P state and react accordingly:
    //@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                Toast.makeText(this, R.string.P2Psuccess, Toast.LENGTH_SHORT).show();
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(this, R.string.P2Pfail, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
