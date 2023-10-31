package com.dazaatar;

import com.dazaatar.tablegenerator.Header;

public class TestClass {
    @Header("Name")
    private String name;
    @Header("Age")
    private int age;
    @Header("Address")
    private String address;
    @Header("Work Address")
    private String workAddress;

    public TestClass(String name, int age, String address, String workAddress) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.workAddress = workAddress;
    }
}
