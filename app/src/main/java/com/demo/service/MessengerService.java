package com.demo.service;

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

import com.demo.util.ConfigUtils;

public class MessengerService extends Service {

    private final String TAG = this.getClass().getSimpleName();

    Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.arg1 == ConfigUtils.MSG_ID_CLIENT) {
                if (msg.getData() == null) {
                    return;
                }
                String content = (String) msg.getData().get(ConfigUtils.MSG_CONTENT);
                Log.d(TAG, "Message from client:" + content);

                //回复客户端信息
                Message replyMsg = Message.obtain();
                replyMsg.arg1 = ConfigUtils.MSG_ID_SERVER;
                Bundle bundle = new Bundle();
                bundle.putString(ConfigUtils.MSG_CONTENT, "收到你的消息了");
                replyMsg.setData(bundle);

                try {
                    //回信
                    msg.replyTo.send(replyMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

}
