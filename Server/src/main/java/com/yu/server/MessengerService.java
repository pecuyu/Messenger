package com.yu.server;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by D22436 on 2017/8/15.
 */

public class MessengerService extends Service {
    private static final int MSG_FROM_CLIENT = 1;
    private static final int MSG_FROM_SERVER = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Bundle data = msg.getData();  // 获取消息数据
                    Log.e("TAG", "msg=" + data.getString("msg"));
                    // 回应
                    Message replyMsg = Message.obtain(null, MSG_FROM_SERVER);
                    Bundle replyData = new Bundle();
                    replyData.putString("replyMsg", "I'm glad to receive from you! have fun");
                    replyMsg.setData(replyData);
                    Messenger client = msg.replyTo;
                    try {
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

            }
        }
    };

    private Messenger messenger = new Messenger(handler);
}
