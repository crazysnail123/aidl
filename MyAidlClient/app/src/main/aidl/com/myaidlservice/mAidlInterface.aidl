// mAidlInterface.aidl
package com.myaidlservice;

// Declare any non-default types here with import statements
import  com.myaidlservice.Person;
import  com.myaidlservice.ResultCallback;
interface mAidlInterface {
    /**
    *
      * AIDL中的定向 tag 表示了在跨进程通信中数据的流向

          in 表示数据只能由客户端流向服务端
          out 表示数据只能由服务端流向客户端
          inout 则表示数据可在服务端与客户端之间双向流通

      其中，数据流向是针对在客户端中的那个传入方法的对象而言的。

          in 为定向 tag 的话表现为服务端将会接收到一个那个对象的完整数据，但是客户端的那个对象不会因为服务端对传参的修改而发生变动；
          out 的话表现为服务端将会接收到那个对象的的空对象，但是在服务端对接收到的空对象有任何修改之后客户端将会同步变动；
          inout 为定向 tag 的情况下，服务端将会接收到客户端传来对象的完整信息，并且客户端将会同步服务端对该对象的任何变动。
    **lry总结：in：服务端能接收到参数,客户端的对象不会随着服务端的对象变化而变化
     * out:服务端接收不到参数，客户端会因为服务端的数据改变而改变
     * inout:客户端的数据能传到服务端，反之也可以
     */
     String greet(String someone);
     String introduce(in Person person, in ResultCallback result);
          Person introduceIn(in Person person);
          Person introduceOut(out Person person);
          Person introduceInOut(inout Person person);
          List<Person> getPersons();
}
