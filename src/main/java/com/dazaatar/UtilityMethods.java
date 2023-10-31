package com.dazaatar;

import com.dazaatar.tablegenerator.Header;

import java.lang.reflect.Field;

public class UtilityMethods {
    public static int generateRandomNumber(){
        return (int)(Math.random() * 10000000);
    }

    public static String getFieldNameWithAnnotation(Field field, Class annotationClass){
        Header annotation = field.getAnnotation(Header.class);
        return annotation != null && !annotation.value().equalsIgnoreCase("DEFAULT") ? annotation.value() : field.getName();
    }
}
