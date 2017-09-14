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
    private static final String TAG = "AidlDemoActivity";
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.editText)
    EditText editText;

    private IMyAidl mAidl;

    private boolean hasConnection, hasMessengerConnection;

    private IOnNewPersonArrivedListener mOnNewPersonArrivedListener = new IOnNewPersonArrivedListener.Stub() {
        @Override
        public void onNewPersonArrived(Person person) throws RemoteException {
            mHandler.obtainMessage(ConfigUtils.MSG_NEW_PERSON_ARRIVED, person).sendToTarget();
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidl = IMyAidl.Stub.asInterface(service);
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    /**
     * 客户端的Messenger
     */
    private Messenger mGetReplayMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigUtils.MSG_ID_SERVER:
                    Log.i(MessengerService.TAG, "receive msg from service:" + msg.getData().getString(ConfigUtils.MSG_CONTENT));
                    break;
                default:
                    super.handleMessage(msg);
            }
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
                Person person = new Person("人物" + random.nextInt(10));
                try {
                    mAidl.addPerson(person);
                    List<Person> personList = mAidl.getPersonList();
                    Log.i(TAG, "query person list, list type:" + personList.getClass().getCanonicalName());
                    Log.i(TAG, "query person list:" + personList.toString());
                    textView2.setText(personList.toString());
                    mAidl.registerListener(mOnNewPersonArrivedListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button3:
                String msgContent = editText.getText().toString();
                Message msg = Message.obtain(null, ConfigUtils.MSG_ID_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putString(ConfigUtils.MSG_CONTENT, msgContent);
                msg.setData(bundle);
                msg.replyTo = mGetReplayMessenger;
                try {
                    mServiceMessenger.send(msg);
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
        if (mAidl != null && mAidl.asBinder().isBinderAlive()) {
            try {
                Log.d(TAG, "unregister listener: " + mOnNewPersonArrivedListener);
                mAidl.unregisterListener(mOnNewPersonArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        if (hasConnection)
            unbindService(mConnection);
        if (hasMessengerConnection)
            unbindService(mMessengerConnection);
        super.onDestroy();
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mAidl == null) {
                return;
            }
            mAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mAidl = null;
            hasConnection = true;
            Intent intent = new Intent(getApplicationContext(), MyAidlService.class);
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConfigUtils.MSG_NEW_PERSON_ARRIVED:
                    Log.d(TAG, "receive new Person:" + msg.obj);
                    textView2.setText(msg.obj.toString());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

}
