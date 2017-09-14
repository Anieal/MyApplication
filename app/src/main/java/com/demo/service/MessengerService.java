package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.demo.util.ConfigUtils;

import org.litepal.util.LogUtil;

public class MessengerService extends Service {

    public static final String TAG = "MessengerService";

    private final Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigUtils.MSG_ID_CLIENT:
                    Log.i(TAG, "receive msg from client: " + msg.getData().getString(ConfigUtils.MSG_CONTENT));
                    Messenger client = msg.replyTo;
                    Message replayMessage = Message.obtain(null, ConfigUtils.MSG_ID_SERVER);
                    Bundle bundle = new Bundle();
                    bundle.putString(ConfigUtils.MSG_CONTENT, "收到您的消息了，稍后会回复您！");
                    replayMessage.setData(bundle);
                    try {
                        client.send(replayMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

}
