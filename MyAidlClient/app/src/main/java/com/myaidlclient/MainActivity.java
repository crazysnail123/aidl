package com.myaidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myaidlservice.Person;
import com.myaidlservice.ResultCallback;
import com.myaidlservice.mAidlInterface;

import java.util.List;

/*
* AIDL中的定向 tag 表示了在跨进程通信中数据的流向

    in 表示数据只能由客户端流向服务端
    out 表示数据只能由服务端流向客户端
    inout 则表示数据可在服务端与客户端之间双向流通

其中，数据流向是针对在客户端中的那个传入方法的对象而言的。

    in 为定向 tag 的话表现为服务端将会接收到一个那个对象的完整数据，但是客户端的那个对象不会因为服务端对传参的修改而发生变动；
    out 的话表现为服务端将会接收到那个对象的的空对象，但是在服务端对接收到的空对象有任何修改之后客户端将会同步变动；
    inout 为定向 tag 的情况下，服务端将会接收到客户端传来对象的完整信息，并且客户端将会同步服务端对该对象的任何变动。

* */
public class MainActivity extends AppCompatActivity {

    private mAidlInterface mInterface;
    private Button bindButton;
    private Button unbindButton;
    private Button helloButton;
    private Person person;
    private final static String TAG = "aidltestclient";
//参考  https://blog.csdn.net/luoyanglizi/article/details/51958091
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInterface = mAidlInterface.Stub.asInterface(service);
            Log.i(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service Disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        person = new Person();
        person.name = "Han";
        person.age = "23";
        person.gender = "male";

        //绑定
        Intent intent = new Intent("co.example.leo.myService");
        //for android 5.0 and later, service intent must be explicit
        intent.setPackage("com.myaidlservice");
        bindService(intent, conn, Context.BIND_AUTO_CREATE);

//        bindButton = (Button)findViewById(R.id.bindButton);
//        bindButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("co.example.leo.myService");
//                //for android 5.0 and later, service intent must be explicit
//                intent.setPackage("com.myaidlservice");
//                bindService(intent, conn, Context.BIND_AUTO_CREATE);
////                unbindButton.setEnabled(true);
////                helloButton.setEnabled(true);
////                bindButton.setEnabled(false);
//            }
//        });
        unbindButton = (Button)findViewById(R.id.unbind_button);
        unbindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(conn);
//                unbindButton.setEnabled(false);
//                bindButton.setEnabled(true);
//                helloButton.setEnabled(false);
            }
        });
        helloButton = (Button)findViewById(R.id.hello_button);
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    String helloText = mInterface.greet("zhangsan");
                    String niceText = mInterface.introduce(person, result);
//                    Toast.makeText(MainActivity.this, helloText, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "返回的数据=="+niceText, Toast.LENGTH_SHORT).show();
                }catch (RemoteException e){
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button    btn_base = (Button)findViewById(R.id.btn_base);
        btn_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String helloText = mInterface.greet("zhangsan");
                    Toast.makeText(MainActivity.this,"传入基本数据，处理后返回---"+ helloText, Toast.LENGTH_SHORT).show();
                }catch (RemoteException e){
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button    btn_in = (Button)findViewById(R.id.btn_in);
        btn_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addPersonIn();
            }
        });
        Button    btn_out = (Button)findViewById(R.id.btn_out);
        btn_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addPersonOut();
            }
        });
        Button    btn_inout = (Button)findViewById(R.id.btn_inout);
        btn_inout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addPersonInOut();
            }
        });
        Button    btn_list = (Button)findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getList();
            }
        });


    }

    public void addPersonIn() {
        //如果与服务端的连接处于未连接状态，则尝试连接

        Person person = new Person();
        person.setGender("nan");
        person.setName("张三In");
        person.setAge("21");
        try {
            //获得服务端执行方法的返回值，并打印输出
            Person returnPerson = mInterface.introduceIn(person);
            //预期结果：传入数据没有变，服务端能接收到参数，服务端修改后客户端的传入数据不会变（和传入前一样）
            Log.e(getLocalClassName(), "传入"+person.toString());
            Log.e(getLocalClassName(), "返回"+returnPerson.toString());
//            Log.e(getLocalClassName(), returnPerson.toString());
        } catch (RemoteException e) {
            e.printStackTrace(); }
    }
    public void addPersonOut() {
        //如果与服务端的连接处于未连接状态，则尝试连接

        Person person = new Person();
        person.setName("李四out");
        person.setGender("nan");
        person.setAge("22");
        try {
            //获得服务端执行方法的返回值，并打印输出
            Person returnPerson = mInterface.introduceOut(person);
            //预期结果：返回后传入数据变成和返回数据一样,服务端接收不到传入的数据
            Log.e(getLocalClassName(), "传入"+person.toString());
            Log.e(getLocalClassName(), "返回"+returnPerson.toString());
        } catch (RemoteException e) {
            e.printStackTrace(); }
    }
    public void addPersonInOut() {
        //如果与服务端的连接处于未连接状态，则尝试连接

        Person person = new Person();
        person.setName("王五InOut");
        person.setAge("23");
        person.setGender("nv");

        try {
            //获得服务端执行方法的返回值，并打印输出
            Person returnPerson = mInterface.introduceInOut(person);
            Log.e(getLocalClassName(), "传入"+person.toString());
            Log.e(getLocalClassName(), "返回"+returnPerson.toString());
//            Log.e(getLocalClassName(), returnPerson.toString());
        } catch (RemoteException e) {
            e.printStackTrace(); }
    }

    public void getList()
    {
        try {
            //获得服务端执行方法的返回值，并打印输出
            List<Person> returnPerson = mInterface.getPersons();
            Log.e(getLocalClassName(), "返回"+returnPerson.toString());
//            Log.e(getLocalClassName(), returnPerson.toString());
        } catch (RemoteException e) {
            e.printStackTrace(); }
    }


    private final ResultCallback.Stub result = new ResultCallback.Stub() {
        @Override
        public void response(Person person) throws RemoteException {
            Toast.makeText(MainActivity.this, "接口回调的数据：---"+person.name+"---"+person.gender, Toast.LENGTH_LONG).show();
        }

        @Override
        public String success(String result) throws RemoteException {
            Log.i(TAG, result);
            return result;
        }

        @Override
        public String failure(String error) throws RemoteException {
            Log.i(TAG, error);
            return error;
        }
    };
}
