package com.e.notisaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class BReceiver extends BroadcastReceiver {
    Context pcontext;

    @Override
    public void onReceive(Context context, Intent intent) {
        pcontext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyService.class));
        } else {
            context.startService(new Intent(context, MyService.class));
        }
        try {
            TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        } catch (Exception e) {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                case TelephonyManager.CALL_STATE_RINGING:
                    Intent dialogIntent = new Intent(pcontext, DialogActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogIntent.putExtra("title", incomingNumber);
                    dialogIntent.putExtra("text", "Call Ended");
                    pcontext.startActivity(dialogIntent);
                    break;
            }
        }
    }
}
