// ResultCallback.aidl
package com.myaidlservice;

// Declare any non-default types here with import statements
import com.myaidlservice.Person;
interface ResultCallback {
     String success(String result);
     String failure(String error);
     void response(in Person person);
}
