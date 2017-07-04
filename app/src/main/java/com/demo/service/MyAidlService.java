package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.demo.IMyAidl;
import com.demo.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class MyAidlService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    private List<Person> mPersons;

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
    };

    /**
     * 客户端与服务端绑定时的回调，返回mIBinder后，客户端就可以通过它远程调用服务器的方法，即实现了通讯
     * @param intent
     * @return 本地Binder对象
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mPersons = new ArrayList<>();
        return mIBinder;
    }
}
