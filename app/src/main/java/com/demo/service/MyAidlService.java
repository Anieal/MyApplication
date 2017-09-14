package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.demo.IMyAidl;
import com.demo.IOnNewPersonArrivedListener;
import com.demo.bean.Person;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyAidlService extends Service {
    private static final String TAG = "MyAidlService";

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Person> mPersons = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewPersonArrivedListener> mListenerList = new RemoteCallbackList<>();
    /**
     * 创建生成本地Binder对象，实现AIDL制定的方法
     */
    private IBinder mIBinder = new IMyAidl.Stub() {
        @Override
        public void addPerson(Person person) throws RemoteException {
            mPersons.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return mPersons;
        }

        @Override
        public void registerListener(IOnNewPersonArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
            Log.d(TAG, "registerListener" + mListenerList.getRegisteredCallbackCount());
        }

        @Override
        public void unregisterListener(IOnNewPersonArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
            Log.d(TAG, "unregisterListener" + mListenerList.getRegisteredCallbackCount());
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.demo.permission.ACCESS_PERSON_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            return !(packageName != null && !packageName.startsWith("com.demo")) && super.onTransact(code, data, reply, flags);

        }
    };

    /**
     * 客户端与服务端绑定时的回调，返回mIBinder后，客户端就可以通过它远程调用服务器的方法，即实现了通讯
     *
     * @param intent
     * @return 本地Binder对象
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.demo.permission.ACCESS_PERSON_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return null;
        }
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new ServiceWorks()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewPersonArrived(Person person) {
        mPersons.add(person);
        final int N = mListenerList.beginBroadcast();
        Log.d(TAG, "onNewPersonArrived, notify listener:" + N);
        for (int i = 0; i < N; i++) {
            IOnNewPersonArrivedListener listener = mListenerList.getBroadcastItem(i);
            if (listener != null) {
                Log.d(TAG, "onNewPersonArrived, notify listener:" + listener);
                try {
                    listener.onNewPersonArrived(person);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mListenerList.finishBroadcast();
    }

    private class ServiceWorks implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random random = new Random();
                Person person = new Person("新增人物" + random.nextInt(10));
                onNewPersonArrived(person);
            }
        }

    }
}
