package com.demo;

import com.demo.bean.Person;

interface IMyAidl {

    void addPerson(in Person person);

    List<Person> getPersonList();

}
