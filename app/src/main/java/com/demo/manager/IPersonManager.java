package com.demo.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.demo.bean.Person;

import java.util.List;

public interface IPersonManager extends IInterface {
    static final String DESCRIPTOR = "com.demo.manager.IPersonManager";

    public class PersonManagerImpl extends Binder implements IPersonManager {

        public PersonManagerImpl() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IPersonManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
            if (iInterface != null && iInterface instanceof IPersonManager) {
                return (IPersonManager) iInterface;
            }
            return new PersonManagerImpl.Proxy(obj);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return null;
        }

        @Override
        public void addPerson(Person person) throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
                case TRANSACTION_getPersonList:
                    data.enforceInterface(DESCRIPTOR);
                    List<Person> personList = this.getPersonList();
                    reply.writeNoException();
                    reply.writeTypedList(personList);
                    return true;
                case TRANSACTION_addPerson:
                    data.enforceInterface(DESCRIPTOR);
                    Person arg0;
                    if (0 != data.readInt()) {
                        arg0 = Person.CREATOR.createFromParcel(data);
                    } else {
                        arg0 = null;
                    }
                    this.addPerson(arg0);
                    reply.writeNoException();
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements IPersonManager {
            private IBinder mRemote;

            private Proxy(IBinder obj) {
                mRemote = obj;
            }

            @Override
            public List<Person> getPersonList() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel replay = Parcel.obtain();
                List<Person> result;
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(TRANSACTION_getPersonList, data, replay, 0);
                    replay.readException();
                    result = replay.createTypedArrayList(Person.CREATOR);
                } finally {
                    replay.recycle();
                    data.recycle();
                }
                return result;
            }

            @Override
            public void addPerson(Person person) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel replay = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (person != null) {
                        data.writeInt(1);
                        person.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    mRemote.transact(TRANSACTION_addPerson, data, replay, 0);
                    replay.readException();
                }finally {
                    replay.recycle();
                    data.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }
        }
    }

    static final int TRANSACTION_getPersonList = IBinder.FIRST_CALL_TRANSACTION;
    static final int TRANSACTION_addPerson = IBinder.FIRST_CALL_TRANSACTION + 1;

    public List<Person> getPersonList() throws RemoteException;

    public void addPerson(Person person) throws RemoteException;
}
