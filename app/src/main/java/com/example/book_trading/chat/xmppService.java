package com.example.book_trading.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class xmppService extends Service {
    public xmppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
