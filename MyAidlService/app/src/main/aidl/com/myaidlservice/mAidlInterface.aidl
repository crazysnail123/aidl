// mAidlInterface.aidl
package com.myaidlservice;

// Declare any non-default types here with import statements
import  com.myaidlservice.Person;
import  com.myaidlservice.ResultCallback;
interface mAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     String greet(String someone);
     String introduce(in Person person, in ResultCallback result);
     Person introduceIn(in Person person);
     Person introduceOut(out Person person);
     Person introduceInOut(inout Person person);
     List<Person> getPersons();
}
