package com.myaidlservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rimon_kou on 14-12-23.
 */
public class Person implements Parcelable {

    public String name;
    public String gender;
    public String age;

    public static final Creator<Person> CREATOR
            = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Person() {
    }

    private Person(Parcel in) {
        name = in.readString();
        gender = in.readString();
        age = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeString(age);
    }

    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的

        name = dest.readString();
        gender = dest.readString();
        age = dest.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
