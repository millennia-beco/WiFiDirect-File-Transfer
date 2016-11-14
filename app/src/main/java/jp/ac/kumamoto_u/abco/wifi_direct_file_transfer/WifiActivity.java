package jp.ac.kumamoto_u.abco.wifi_direct_file_transfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

/**
 * Created by AbeKodai on 2016/11/14.
 */
public class WifiActivity extends WifiP2pManager {
    WifiP2pManager mManager;
    //WifiP2pManager.Channel ：　アプリケーションをWi-Fi P2Pフレームワークに接続するために使用される
    Channel mChannel;
    BroadcastReceiver mReceiver;

    //@Override
    protected void onCreate(Bundle savedInstanceState){
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null); //ここでinitialize！ Channelを返すらしい
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
    }

    //@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }
        }
    }
}
