package com.yu.client;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final int MSG_FROM_CLIENT = 1;
    private static final int MSG_FROM_SERVER = 2;

    Messenger mMessenger;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FROM_SERVER:
                    Bundle data = msg.getData();
                    Log.e("TAG", data.getString("replyMsg"));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessenger = new Messenger(handler);
        Intent intent = new Intent();
        intent.setClassName("com.yu.server", "com.yu.server.MessengerService");
        startService(intent);
        MessengerServiceConnection conn = new MessengerServiceConnection();
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    class MessengerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("TAG", "onServiceConnected");
            Messenger server = new Messenger(service);
            Message msg = Message.obtain(null,MSG_FROM_CLIENT);
            Bundle data = new Bundle();
            data.putString("msg", "this is msg from client");
            msg.setData(data);
            msg.replyTo=mMessenger;
            try {
                server.send(msg);   // 向服务端发送消息
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

}
