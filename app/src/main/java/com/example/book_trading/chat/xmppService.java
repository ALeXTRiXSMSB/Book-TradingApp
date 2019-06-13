package com.example.book_trading.chat;


import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.example.book_trading.chat.Nachricht.ChatDatabase;
import com.example.book_trading.chat.Nachricht.ChatNachricht;
import com.example.book_trading.chat.Nachricht.ChatNachrichtDAO;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.forward.packet.Forwarded;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.mam.MamManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class xmppService extends Service {


    private String empfaenger;
    private static final String XMPPDOMAIN = "booktrading";
    private static final String IP = "192.168.178.34";

    //TODO: EMPFANGER von der Datenbank abfragen -> möglicherweise Problem mit mehreren Teilhabern


    private String username;
    private String password;

    private ChatDatabase chatDatabase;
    private ChatNachrichtDAO chatNachrichtDAO;

    private Boolean account_exist;
    private BroadcastReceiver gesendet;
    private chatBroadcastReceiver receiver;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        super.onStartCommand(intent, flags, startId);

        build_Database();
        Bundle extras = intent.getExtras();

        this.username = extras.getString("USERNAME");
        this.password = extras.getString("PASSWORD");


        this.account_exist = extras.getBoolean("ACCOUNT_EXIST");


        /**registerReceiver(): Broadcast wird registriert
         * registerReceiver_empfangen: Broadcast wird empfangen (EMPFAENGER und USERNAME)*/
        registerReceiver(xmppService.this);
        registerReceiver_empfangen();

        new Thread(new Runnable() {
            public void run() {
                login(username, password);
                receiveMessages();

            }
        }).start();


        return START_NOT_STICKY;
    }


    public void build_Database() {


        chatDatabase = ChatDatabase.getChatDatabase(getApplicationContext());

        chatNachrichtDAO = chatDatabase.getChatNachrichtDAO();

    }


    //TODO: MAM Manager verstehen: welche NAchricht kommt zurück, welche jid? etc...
    private void mam_new_new(Jid jid) {
        try {
            MamManager mamManager = MamManager.getInstanceFor(connection);
            boolean isSupported = mamManager.isSupported();
            if (isSupported) {

                MamManager.MamQueryArgs mamQueryArgs = MamManager.MamQueryArgs.builder()
                        .limitResultsToJid(jid)
                        .setResultPageSizeTo(10)
                        .queryLastPage()
                        .build();
                MamManager.MamQuery mamQuery = mamManager.queryArchive(mamQueryArgs);
                List<Message> messages = mamQuery.getMessages();
                for (Message message : messages) {
                    Log.d("Mam funzt: Nachricht ", message.getBody());
                    Log.d("Mam funzt: Sender ", message.getFrom().toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AbstractXMPPConnection connection;
    private ChatManager chatManager;


    public void login(String userName, String password) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(IP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        XMPPTCPConnectionConfiguration config = null;
        try {
            config = XMPPTCPConnectionConfiguration.builder().


                    setUsernameAndPassword(userName, password)
                    .setXmppDomain(XMPPDOMAIN)
                    .setHostAddress(inetAddress)
                    .setCompressionEnabled(true)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setResource("Android")

                    .setPort(5222)
                    .build();
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }


        connection = new XMPPTCPConnection(config);
        connection.setReplyTimeout(30000);


        try {
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /**falls Account existiert wird registriert*/
        if (!account_exist) {
            register();
        }


        try {
            connection.login();

        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chatManager = ChatManager.getInstanceFor(connection);
    }

    public void register() {
        try {
            AccountManager accountManager = AccountManager.getInstance(connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);

            accountManager.createAccount(Localpart.from(username), password);

        } catch (SmackException | XMPPException | XmppStringprepException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * einkommende Nachricht wird hier abgefangen und als Broadcast zum Receiver und von diesem zu MainActivity
     */
    public void receiveMessages() {

        chatManager.addIncomingListener(new IncomingChatMessageListener() {


            @Override
            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                String chat_partner = from.toString().replace("@booktrading", "");
                sendBroadcast(xmppService.this, message.getBody(), chat_partner);

                /**aktualiesierung der Datenbank*/


                ChatNachricht nachricht = new ChatNachricht(false, message.getBody(), getID(), chat_partner);

                nachricht.setLeft(false);
                nachricht.setMessage(message.getBody());
                nachricht.setTo(chat_partner);

                chatNachrichtDAO.insert(nachricht);
                /**----------------------------------------*/

                Log.d("Nachricht empfangen", from.toString() + "\n" + message.getBody() + "\n" + chat);

            }
        });


    }


    public void sendMessage(String message, String to) {


        Chat chat = chatManager.chatWith(build_jid(to));
        try {


            chat.send(message);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private EntityBareJid build_jid(String to) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(to + "@" + XMPPDOMAIN);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        return jid;
    }

    public void sendBroadcast(Context context, String msg, String to) {
        Intent intent = new Intent(chatBroadcastReceiver.BC_ACTION_EMPFANGEN);
        intent.putExtra("EMPFANGEN", msg);
        intent.putExtra("SENDER", to);
        context.sendBroadcast(intent);
    }

    private void registerReceiver(Context context) {
        receiver = new chatBroadcastReceiver();
        IntentFilter filter = new IntentFilter(chatBroadcastReceiver.BC_ACTION_EMPFANGEN);
        filter.setPriority(100);
        context.registerReceiver(receiver, filter);
    }


    private void registerReceiver_empfangen() {
        gesendet = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(chatBroadcastReceiver.BC_INTENT_GESENDET)) {
                    Bundle b = intent.getExtras();
                    String message = b.getString("MESSAGE");
                    empfaenger = b.getString("EMPFAENGER");
                    //mam_new_new(build_jid(empfaenger));

                    sendMessage(message, empfaenger);

                }
            }
        };
        registerReceiver(gesendet, new IntentFilter(chatBroadcastReceiver.BC_INTENT_GESENDET));
    }


    public xmppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * ID ist die aktuelle zeit in millisekunden
     */
    public long getID() {
        long id = System.currentTimeMillis();


        return id;
    }


    @Override
    public void onDestroy() {
        try {
            super.onDestroy();

            connection.disconnect();
            unregisterReceiver(receiver);
            unregisterReceiver(gesendet);


        } catch (Exception e) {
            Log.d("XmppService", "gestoppt");
        }

    }


}
