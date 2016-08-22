package com.oceanmanaus.libocean.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;

import com.oceanmanaus.libocean.Ocean;

/**
 * Created by oceanmanaus on 22/08/2016.
 */
public abstract class OceanFragment extends Fragment {

    private ConexaoReceiver mReceiver;

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new ConexaoReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    protected void isConnected(){};

    class ConexaoReceiver extends BroadcastReceiver {
        boolean mPrimeiraVez = true;
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPrimeiraVez) {
                mPrimeiraVez = false;
                return;
            }
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (Ocean.isConnected(context)) {
                    isConnected();
                }
            }
        }
    }
}