package com.demo;

import com.demo.bean.Person;
import com.demo.IOnNewPersonArrivedListener;

interface IMyAidl {

    void addPerson(in Person person);

    List<Person> getPersonList();

    void registerListener(IOnNewPersonArrivedListener listener);

    void unregisterListener(IOnNewPersonArrivedListener listener);

}
