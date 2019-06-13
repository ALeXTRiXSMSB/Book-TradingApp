package com.example.book_trading.chat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;

public class chatLogin {
    private Context context;

    public chatLogin(String username, String password, boolean account_exist, Context context) {
        this.context = context;

        registerReceiver();


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            /**wenn der Service noch nicht gestartet ist wird er jetzt gestartet (Anmeldung bzw. Registrierung)*/
            if (!(isXmppServiceRunning(xmppService.class))) {
                startBroadcastLogin(username, password, account_exist);
            }

        } else {
            if (!(isXmppServiceRunning(xmppService.class))) {
                startBroadcastLogin(username, password, account_exist);
            }
        }

    }


    /**
     * ---------------------------------------Chat Methoden------------------------------------------------------------------
     */

    chatBroadcastReceiver receiver;

    /**
     * falls der Service für Xmpp schon läuft wird er nicht nochmal gestartet beim anmelden
     */
    private boolean isXmppServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Login wird dem BroadCastReceiver überreicht, diser meldet dann an
     */
    public void startBroadcastLogin(String USERNAME, String PASSWORD, boolean ACCOUNT_EXIST) {


        Intent intent = new Intent(chatBroadcastReceiver.BC_LOGIN_GESENDET);
        intent.putExtra("USERNAME", USERNAME);
        intent.putExtra("PASSWORD", PASSWORD);
        intent.putExtra("ACCOUNT_EXIST", ACCOUNT_EXIST);
        context.sendBroadcast(intent);


    }

    private void registerReceiver() {
        receiver = new chatBroadcastReceiver();
        IntentFilter filter = new IntentFilter(chatBroadcastReceiver.BC_LOGIN_GESENDET);
        filter.setPriority(100);
        context.registerReceiver(receiver, filter);
    }


    /**
     * Intent zur Chat Übersicht
     */
    public static void startChatActivity(Context context) {
        Intent i = new Intent(context, chat_uebersichtActivity.class);
        context.startActivity(i);
    }
}


