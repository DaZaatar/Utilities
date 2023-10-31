package com.dazaatar;

import com.dazaatar.tablegenerator.HTMLTableGenerator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, ParserConfigurationException, TransformerException {
        TestClass test1 = new TestClass("Anthony", 26, "Jdeideh", "Hamra");
        TestClass test2 = new TestClass("Mario", 33, "Jdeideh", "Bikfaya");

        List<TestClass>  testClassList = new ArrayList<>();
        testClassList.add(test1);
        testClassList.add(test2);

        HTMLTableGenerator<TestClass> testClassHTMLTableGenerator = new HTMLTableGenerator<>(TestClass.class);
        testClassHTMLTableGenerator.setData(testClassList);
        String htmlTable = testClassHTMLTableGenerator.build();

        System.out.println(htmlTable);
    }
}