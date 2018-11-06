package com.myaidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AIDLServer extends Service {

    private final static String TAG = "aidlService";
    //包含Book对象的list
    private List<Person> personList = new ArrayList<>();;

    @Override
    public void onCreate() {
        super.onCreate();
        Person  person = new Person();
        person.name = "Hanaa";
        person.age = "100";
        person.gender = "male";
        personList.add(person);
    }

    private final mAidlInterface.Stub mbinder = new mAidlInterface.Stub() {
        @Override
        public String greet(String someone) throws RemoteException {
            //传入基本数据，处理后返回
            return "hello " + someone;
        }

        @Override
        public String introduce(Person person, ResultCallback result) throws RemoteException {
            String nice = "";
            person.gender = "changed";
            try{
                nice = "nice to meet you, " + person.name;
                nice += " " + result.success("success");
                result.response(person);
            }catch(Exception e){
                nice += result.failure("error");
            }
            return nice;
        }

        @Override
        public Person introduceIn(Person person) {
            synchronized (this) {
                if (person == null) {
                    Log.e(TAG, "Persion in 是空的（introduceIn）");
                    person = new Person();
                }
                //尝试修改参数，主要是为了观察其到客户端的反馈
                person.setName("in");
                person.setAge("66");
                if (!personList.contains(person))
                {
                    personList.add(person);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + personList.toString());
                return person;
            }
        }

        @Override
        public Person introduceOut(Person person) {
            synchronized (this) {
                if (person == null) {
                    Log.e(TAG, "Persion out 是空的（introduceOut）");
                    person = new Person();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                person.setName("out");
                person.setAge("out88");
                if (!personList.contains(person))
                {
                    personList.add(person);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + personList.toString());
                return person;
            }
        }

        @Override
        public Person introduceInOut(Person person) {
            synchronized (this) {
                if (person == null) {
                    Log.e(TAG, "Persion in 是空的（introduceInOut）");
                    person = new Person();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                person.setName("inout");
                person.setAge("99");
                if (!personList.contains(person))
                {
                    personList.add(person);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + personList.toString());
                return person;
            }
        }

        @Override
        public List<Person> getPersons() {

            return personList;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind() is called");
        return true;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy() is called");
        super.onDestroy();
    }
}
