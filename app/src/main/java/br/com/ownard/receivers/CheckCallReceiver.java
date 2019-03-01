package br.com.ownard.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import java.lang.reflect.Method;

import br.com.ownard.factory.CallController;



public class CheckCallReceiver extends BroadcastReceiver {

    private CallController callController;
    public static final String TAG = "PHONE STATE";
    private static String mLastState;

    @Override
    public void onReceive(final Context context, final Intent intent) {


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            mLastState = state;

            if (callController == null) {
                callController = new CallController(context);
            }

            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            try {

                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                if (callController.checkPhone(number)) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        Class c = Class.forName(tm.getClass().getName());
                        Method m = c.getDeclaredMethod("getITelephony");
                        m.setAccessible(true);
                        Object telephonyService = m.invoke(tm);

                        c = Class.forName(telephonyService.getClass().getName());
                        m = c.getDeclaredMethod("endCall");
                        m.setAccessible(true);
                        m.invoke(telephonyService);

                        Toast.makeText(context, "BlockCallEasier - " + number, Toast.LENGTH_SHORT).show();
                        callController.addBlockCount(number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



}
