package com.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.bean.Person;
import com.demo.service.MessengerService;
import com.demo.service.MyAidlService;
import com.demo.util.ConfigUtils;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AidlDemoActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.editText)
    EditText editText;

    private IMyAidl mAidl;

    private boolean hasConnection, hasMessengerConnection;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidl = IMyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    /**
     * 客户端的Messenger
     */
    private Messenger mClientMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null && msg.arg1 == ConfigUtils.MSG_ID_SERVER) {
                if (msg.getData() == null) {
                    return;
                }
                String content = (String) msg.getData().get(ConfigUtils.MSG_CONTENT);
                Log.d(TAG, "Message from server: " + content);
            }
            super.handleMessage(msg);
        }
    });

    private Messenger mServiceMessenger;

    private ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceMessenger = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                hasConnection = true;
                Intent intent = new Intent(getApplicationContext(), MyAidlService.class);
                bindService(intent, mConnection, BIND_AUTO_CREATE);
                Toast.makeText(this, "绑定服务", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Random random = new Random();
                Person person = new Person("Shi xin" + random.nextInt(10));
                try {
                    mAidl.addPerson(person);
                    List<Person> personList = mAidl.getPersonList();
                    textView2.setText(personList.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button3:
                String msgContent = editText.getText().toString();

                Message message = Message.obtain();
                message.arg1 = ConfigUtils.MSG_ID_CLIENT;
                Bundle bundle = new Bundle();
                bundle.putString(ConfigUtils.MSG_CONTENT, msgContent);
                message.setData(bundle);
                message.replyTo = mClientMessenger;//指定回信人是客户端定义的
                try {
                    mServiceMessenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button4:
                hasMessengerConnection = true;
                Intent intent1 = new Intent(this, MessengerService.class);
                bindService(intent1, mMessengerConnection, BIND_AUTO_CREATE);
                Toast.makeText(this, "绑定Messenger服务", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hasConnection)
            unbindService(mConnection);
        if (hasMessengerConnection)
            unbindService(mMessengerConnection);
    }

}
