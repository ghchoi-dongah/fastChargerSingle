package com.dongah.fastcharger.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import service.vcat.smartro.com.vcat.SmartroVCatInterface;

public class ServicePaymentFragment extends Fragment {

    private static final Logger logger = LoggerFactory.getLogger(ServicePaymentFragment.class);

    private static final String SERVER_PACKAGE = "service.vcat.smartro.com.vcat";
    private static final String SERVER_ACTION = "smartro.vcat.action";


    /** service instance */
    public SmartroVCatInterface mSmartroVCatInterface = null;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSmartroVCatInterface = SmartroVCatInterface.Stub.asInterface(service);
            try {
                connectedWithService();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            disconnectedWithService();
        }
    };

    private void disconnectedWithService() {
        mSmartroVCatInterface = null;
    }

    private void connectedWithService() {
    }

    protected SmartroVCatInterface getVCatInterface() {
        return mSmartroVCatInterface;
    }

    protected void writeLog(String strText)
    {
        logger.debug(strText);
    }
    private String mStrToastMessage = null;

    protected void showToast(String strMessage)
    {
        mStrToastMessage = strMessage;
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Toast.makeText(getActivity(), mStrToastMessage, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intentTemp = new Intent(SERVER_ACTION);
        intentTemp.setPackage(SERVER_PACKAGE);
        if (getActivity() != null) {
            getActivity().bindService(intentTemp, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onDetach() {
        if (getActivity() != null) {
            getActivity().unbindService(serviceConnection);
        }
        super.onDetach();
    }

}

